package com.programasoft.application.advocate

import com.programasoft.application.account.Account
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface AdvocateRepository : JpaRepository<Advocate, Long> {
    fun findByAccount(account: Account): Advocate?

    @Query(
        "SELECT ag.advocate " +
                "FROM AvailabilityUnit au " +
                "JOIN au.availability av " +
                "JOIN av.availabilityGroup ag " +
                "WHERE au.id = :availabilityUnitId"
    )
    fun findAdvocateByAvailabilityUnitId(@Param("availabilityUnitId") availabilityUnitId: Long): Advocate?
}