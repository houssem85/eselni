package com.programasoft.application.availability

import com.programasoft.application.account.Account
import com.programasoft.application.advocate.Advocate
import com.programasoft.application.reservation.Reservation
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.TemporalAdjusters
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

    fun getAvailabilitiesForPeriodByAdvocate(
        start: LocalDate,
        end: LocalDate,
        advocate: Advocate
    ): List<Availability> {
        return availabilityRepository.findAvailabilitiesForPeriodByAdvocate(
            start.atStartOfDay(),
            end.atTime(LocalTime.MAX),
            advocate
        )
    }

    fun getAvailableDaysByAdvocateAndMonth(month: Int, year: Int, advocate: Advocate): List<Date> {
        return availabilityUnitRepository.findAvailableDaysByAdvocateAndMonth(
            advocateId = advocate.id,
            month = month,
            year = year
        )
    }

    fun getAvailabilityUnitsByAdvocateAndDate(advocateId: Long, date: LocalDate): List<AvailabilityUnit> {
        return availabilityUnitRepository.findByAdvocateIdAndDate(advocateId, date)
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