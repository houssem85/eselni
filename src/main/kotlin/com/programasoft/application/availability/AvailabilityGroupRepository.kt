package com.programasoft.application.availability

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface AvailabilityGroupRepository : JpaRepository<AvailabilityGroup, Long> {
    @Query("SELECT COUNT(au)\n" +
            "FROM AvailabilityUnit au\n" +
            "JOIN au.availability a\n" +
            "JOIN a.availabilityGroup ag\n" +
            "WHERE ag.id = :groupId AND au.reservation IS NOT NULL")
    fun countReservedAvailabilityUnitsByGroupId(groupId: Long): Int

    fun findByPsychologistId(psychologistId: Long): List<AvailabilityGroup>
}