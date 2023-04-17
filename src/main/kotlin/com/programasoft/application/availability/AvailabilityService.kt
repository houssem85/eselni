package com.programasoft.application.availability

import com.programasoft.application.psychologist.Psychologist
import com.programasoft.application.reservation.Reservation
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

@Service
class AvailabilityService(
    private val availabilityRepository: AvailabilityRepository,
    private val availabilityUnitRepository: AvailabilityUnitRepository,
    private val availabilityGroupRepository: AvailabilityGroupRepository,
) {


    fun createAvailabilityGroup(availabilityGroup: AvailabilityGroup): AvailabilityGroup {
        return availabilityGroupRepository.save(availabilityGroup)
    }

    fun deleteAvailabilityGroup(id: Long) {
        availabilityGroupRepository.deleteById(id)
    }

    fun countReservedAvailabilityUnitsByGroupId(id: Long): Int {
        return availabilityGroupRepository.countReservedAvailabilityUnitsByGroupId(id)
    }

    fun createAvailabilities(list: List<Availability>): List<Availability> {
        return availabilityRepository.saveAll(list)
    }

    fun createAvailabilitiesUnit(list: List<AvailabilityUnit>) {
        availabilityUnitRepository.saveAll(list)
    }

    fun getAvailabilitiesForPeriodByPsychologist(
        start: LocalDate,
        end: LocalDate,
        psychologist: Psychologist
    ): List<Availability> {
        return availabilityRepository.findAvailabilitiesForPeriodByPsychologist(
            start.atStartOfDay(),
            end.atTime(LocalTime.MAX),
            psychologist
        )
    }

    fun getAvailableDaysByPsychologistAndMonth(month: Int, year: Int, psychologist: Psychologist): List<Date> {
        return availabilityUnitRepository.findAvailableDaysByPsychologistAndMonth(
            psychologistId = psychologist.id,
            month = month,
            year = year
        )
    }

    fun getAvailabilityUnitsByPsychologistAndDate(psychologistId: Long, date: LocalDate): List<AvailabilityUnit> {
        return availabilityUnitRepository.findByPsychologistIdAndDate(psychologistId, date)
    }

    fun updateAvailabilityUnitReservation(availabilityUnitId: Long, reservation: Reservation): AvailabilityUnit {
        val availabilityUnit = availabilityUnitRepository.findById(availabilityUnitId)
            .orElseThrow { EntityNotFoundException("AvailabilityUnit with id $availabilityUnitId not found") }
        return availabilityUnitRepository.save(
            availabilityUnit.copy(
                reservation = reservation
            )
        )
    }

    fun getAllByReservationId(reservationId: Long): List<AvailabilityUnit> {
        return availabilityUnitRepository.findAllByReservationId(reservationId)
    }
}