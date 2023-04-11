package com.programasoft.application.availability

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.*

@Repository
interface AvailabilityUnitRepository : JpaRepository<AvailabilityUnit, Long> {

    @Query("SELECT DISTINCT DATE(au.start) \n" +
            "FROM AvailabilityUnit au \n" +
            "JOIN au.availability av \n" +
            "JOIN av.availabilityGroup ag \n" +
            "WHERE YEAR(au.start) = :year \n" +
            "AND MONTH(au.start) = :month \n" +
            "AND au.reservation IS NULL \n" +
            "AND ag.advocate.id = :advocateId")
    fun findAvailableDaysByAdvocateAndMonth(
        @Param("advocateId") advocateId: Long,
        @Param("year") year: Int,
        @Param("month") month: Int
    ): List<Date>

}