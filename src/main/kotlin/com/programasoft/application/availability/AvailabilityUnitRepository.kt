package com.programasoft.application.availability

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@Repository
interface AvailabilityUnitRepository : JpaRepository<AvailabilityUnit, Long> {

    @Query(
        "SELECT DISTINCT DATE(au.start) \n" +
                "FROM AvailabilityUnit au \n" +
                "JOIN au.availability av \n" +
                "JOIN av.availabilityGroup ag \n" +
                "WHERE au.start > NOW() \n" +
                "AND au.reservation IS NULL \n" +
                "AND ag.psychologist.id = :psychologistId"
    )
    fun findAvailableDaysByPsychologistAndMonth(
        @Param("psychologistId") psychologistId: Long
    ): List<Date>

    @Query(
        """
    SELECT au
    FROM AvailabilityUnit au
    JOIN au.availability av
    JOIN av.availabilityGroup ag
    WHERE ag.psychologist.id = :psychologistId
    AND av.start > :startDate
    AND av.start < :endDate
    AND au.reservation IS NULL
    """
    )
    fun findByPsychologistIdAndDate(
        @Param("psychologistId") psychologistId: Long,
        @Param("startDate") startDate: LocalDateTime,
        @Param("endDate") endDate: LocalDateTime,
    ): List<AvailabilityUnit>

    fun findAllByReservationId(reservationId: Long): List<AvailabilityUnit>


    fun findByReservationId(reservationId: Long): List<AvailabilityUnit>
}