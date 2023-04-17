package com.programasoft.application.availability

import com.programasoft.application.psychologist.Psychologist
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface AvailabilityRepository : JpaRepository<Availability, Long> {
    @Query("SELECT a FROM Availability a WHERE a.start >= :start AND a.end <= :end AND a.availabilityGroup.psychologist = :psychologist")
    fun findAvailabilitiesForPeriodByPsychologist(@Param("start") start: LocalDateTime, @Param("end") end: LocalDateTime, @Param("psychologist") psychologist: Psychologist): List<Availability>
}