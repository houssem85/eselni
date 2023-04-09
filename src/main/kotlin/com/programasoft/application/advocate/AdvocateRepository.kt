package com.programasoft.application.advocate

import com.programasoft.application.account.Account
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AdvocateRepository : JpaRepository<Advocate, Long> {
    fun findByAccount(account: Account): Advocate?
}