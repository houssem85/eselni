package com.programasoft.application.psychologist

import com.programasoft.application.account.Account
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface PsychologistRepository : JpaRepository<Psychologist, Long> {
    fun findByAccount(account: Account): Psychologist?

    @Query(
        "SELECT ag.psychologist " +
                "FROM AvailabilityUnit au " +
                "JOIN au.availability av " +
                "JOIN av.availabilityGroup ag " +
                "WHERE au.id = :availabilityUnitId"
    )
    fun findPsychologistByAvailabilityUnitId(@Param("availabilityUnitId") availabilityUnitId: Long): Psychologist?
}