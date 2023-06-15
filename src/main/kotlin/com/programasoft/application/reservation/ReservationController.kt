package com.programasoft.application.reservation

import com.programasoft.application.availability.AvailabilityService
import com.programasoft.application.client.ClientService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*


@RequestMapping("api/v1/reservations")
@RestController
class ReservationController(
    private val availabilityService: AvailabilityService,
    private val reservationService: ReservationService,
    private val clientService: ClientService,
) {

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    fun saveReservation(@RequestBody request: CreateReservationRequest) {
        val client = clientService.getById(request.clientId)
        val reservation = Reservation(
            id = 0,
            client = client
        )
        val newReservation = reservationService.create(reservation)
        request.availabilityUnitIds.forEach {
            availabilityService.updateAvailabilityUnitReservation(it, newReservation)
        }
    }

    @GetMapping("client/{clientId}")
    @ResponseStatus(HttpStatus.OK)
    fun getReservationsByClient(
        @PathVariable("clientId") clientId: Long,
        @RequestParam isPaid: Boolean = true
    ): List<ReservationResponse> {
        val list: List<Reservation> = if (isPaid) {
            reservationService.getPaidReservationsByClient(clientId)
        } else {
            reservationService.getNotPaidReservationsByClient(clientId)
        }
        return list.map {
            val availabilitiyUnits = availabilityService.getAvailabilityUnitByReservation(it.id)
            Collections.sort(availabilitiyUnits) { unit1, unit2 ->
                unit1.start.compareTo(unit2.start)
            }
            it.copy(
                availabilityUnits = availabilitiyUnits.toSet()
            )
        }.sortedWith(compareByDescending<Reservation> { reservation ->
            reservation.availabilityUnits.sortedBy { it.start }.firstOrNull()?.start
        }.thenBy { it.date }).map {
            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("YYYY-MM-dd", Locale.ENGLISH)
            val formattedDate: String = it.availabilityUnits.first().start.toLocalDate().format(formatter)
            var totalDuration = Duration.ZERO
            for (availabilityUnit in it.availabilityUnits) {
                val intervalDuration = Duration.between(availabilityUnit.start, availabilityUnit.end)
                totalDuration += intervalDuration
            }
            ReservationResponse(
                id = it.id,
                psychologist = it.availabilityUnits.first().availability?.availabilityGroup?.psychologist?.account?.fullName
                    ?: "",
                date = formattedDate,
                startTime = it.availabilityUnits.first().start.toString().takeLast(5),
                endTime = it.availabilityUnits.last().end.toString().takeLast(5),
                isPaid = isPaid,
                amount = totalDuration.toMinutes() * ((it.availabilityUnits.first().availability?.availabilityGroup?.psychologist?.hourlyRate
                    ?: 0f) / 60)
            )
        }
    }

    @GetMapping("ready/client/{clientId}")
    @ResponseStatus(HttpStatus.OK)
    fun getReadyReservationsByClient(
        @PathVariable("clientId") clientId: Long,
    ): List<ReservationReadyResponse> {
        val now = LocalDate.of(2023, 6, 21)
        val startOfDay = LocalDateTime.of(now, LocalTime.MIN)
        val dateTime = LocalDateTime.of(now, LocalDateTime.now().toLocalTime())
        val list = reservationService.getPaidReservationsOfCurrentDayByClient(
            clientId,
            dateTime,
            startOfDay,
        )
        val result = list.map {
            val availabilitiyUnits = availabilityService.getAvailabilityUnitByReservation(it.id)
            Collections.sort(availabilitiyUnits) { unit1, unit2 ->
                unit1.start.compareTo(unit2.start)
            }
            it.copy(
                availabilityUnits = availabilitiyUnits.toSet()
            )
        }.sortedWith(compareBy<Reservation> { reservation ->
            reservation.availabilityUnits.sortedBy { it.start }.firstOrNull()?.start
        }.thenBy { it.date }).map {
            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("YYYY-MM-dd", Locale.ENGLISH)
            val formattedDate: String = it.availabilityUnits.first().start.toLocalDate().format(formatter)
            var totalDuration = Duration.ZERO
            for (availabilityUnit in it.availabilityUnits) {
                val intervalDuration = Duration.between(availabilityUnit.start, availabilityUnit.end)
                totalDuration += intervalDuration
            }
            ReservationReadyResponse(
                id = it.id,
                psychologist = it.availabilityUnits.first().availability?.availabilityGroup?.psychologist?.account?.fullName
                    ?: "",
                client = it.client?.account?.fullName ?: "",
                date = formattedDate,
                startTime = it.availabilityUnits.first().start.toString().takeLast(5),
                endTime = it.availabilityUnits.last().end.toString().takeLast(5),
                isReadyForJoin = it.availabilityUnits.first().start.isBefore(dateTime)
            )
        }
        return result
    }

}