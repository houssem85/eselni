package com.programasoft.application.availability

import com.programasoft.application.psychologist.PsychologistService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

@RestController
@RequestMapping("/api/v1/availabilities")
class AvailabilityController(
    private val availabilityService: AvailabilityService,
    private val psychologistService: PsychologistService,
) {

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    fun createAvailabilities(@RequestBody request: AvailabilityRequest): AvailabilityGroup {
        val startDate = request.startDate
        val endDate = request.endDate

        val psychologist = psychologistService.getById(request.psychologistId)

        val availabilityGroup = AvailabilityGroup(
            id = 0,
            startDate = startDate,
            endDate = endDate,
            psychologist = psychologist
        )

        val availabilities = arrayListOf<Availability>()

        var date = startDate
        while (date <= endDate) {
            val dayOfWeek = date.dayOfWeek
            val availabilitiesForDay = request.days[dayOfWeek.name.lowercase()]?.map { availability ->
                val startTime = LocalTime.parse(availability.start)
                val endTime = LocalTime.parse(availability.end)
                val availability = Availability(
                    id = 0,
                    availabilityGroup = availabilityGroup,
                    start = date.atTime(startTime),
                    end = date.atTime(endTime),
                )
                return@map availability
            } ?: listOf()

            availabilities.addAll(availabilitiesForDay)

            date = date.plusDays(1)
        }

        val oldAvailabilitiesInThisPeriod = availabilityService.getAvailabilitiesForPeriodByPsychologist(
            start = startDate, end = endDate, psychologist = psychologist
        )

        val isOverlap = checkAvailabilityOverlap(oldAvailabilitiesInThisPeriod, availabilities)
        if (!isOverlap) {
            val newAvailabilityGroup = availabilityService.createAvailabilityGroup(availabilityGroup)

            val newAvailabilities = availabilityService.createAvailabilities(availabilities.map {
                it.copy(
                    availabilityGroup = newAvailabilityGroup
                )
            })
            newAvailabilities.forEach { availability ->
                val units = createAvailabilityUnits(availability)
                availabilityService.createAvailabilitiesUnit(units)
            }
            return availabilityGroup
        } else {
            throw ResponseStatusException(HttpStatus.CONFLICT, "HTTP Status will be CONFLICT (CODE 409)\n")
        }
    }

    fun checkAvailabilityOverlap(list1: List<Availability>, list2: List<Availability>): Boolean {
        val sortedList1 = list1.sortedBy { it.start }
        val sortedList2 = list2.sortedBy { it.start }

        var i = 0
        var j = 0

        while (i < sortedList1.size && j < sortedList2.size) {
            val avail1 = sortedList1[i]
            val avail2 = sortedList2[j]

            if (avail1.start.isBefore(avail2.end) && avail2.start.isBefore(avail1.end)) {
                // Il y a un chevauchement entre les plages horaires
                return true
            }

            if (avail1.end.isBefore(avail2.start)) {
                i++
            } else {
                j++
            }
        }

        // Aucun chevauchement trouvé
        return false
    }

    fun createAvailabilityUnits(availability: Availability): List<AvailabilityUnit> {
        val units = mutableListOf<AvailabilityUnit>()
        var startTime = availability.start
        while (startTime.isBefore(availability.end)) {
            val endTime = startTime.plusMinutes(30)
            if (endTime.isAfter(availability.end)) {
                units.add(
                    AvailabilityUnit(
                        id = 0, availability = availability, start = startTime, end = availability.end
                    )
                )
            } else {
                units.add(AvailabilityUnit(id = 0, availability = availability, start = startTime, end = endTime))
            }
            startTime = endTime
        }
        return units
    }


    @DeleteMapping("/{id}")
    fun deleteAvailabilityGroup(@PathVariable id: Long): ResponseEntity<Void> {
        return if (availabilityService.countReservedAvailabilityUnitsByGroupId(id) > 0) {
            ResponseEntity.status(401).build()
        } else {
            availabilityService.deleteAvailabilityGroup(id)
            ResponseEntity.status(200).build()
        }
    }

    @GetMapping("/available-days")
    fun getAvailableDays(
        @RequestParam psychologistId: Long
    ): ResponseEntity<List<String>> {
        val psychologist = psychologistService.getById(psychologistId)
        val availableDays = availabilityService.getAvailableDaysByPsychologistAndMonth(psychologist).map {
            it.toString()
        }
        return ResponseEntity.ok(availableDays)
    }

    @GetMapping("/available-units")
    @ResponseStatus(HttpStatus.OK)
    fun getAvailabilityUnits(
        @RequestParam("psychologistId") psychologistId: Long,
        @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") date: LocalDate
    ): List<AvailabilityUnit> {
        val units = availabilityService.getAvailabilityUnitsByPsychologistAndDate(psychologistId, date)
        return units.map {
            it.copy(availability = null)
        }
    }

}