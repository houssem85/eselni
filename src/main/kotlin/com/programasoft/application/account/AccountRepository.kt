package com.programasoft.application.account

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountRepository : JpaRepository<Account, Long>{

    fun findByEmail(email: String): Account?

    fun existsByEmail(email: String): Boolean {
        return findByEmail(email) != null
    }

    fun findByEmailAndPassword(email: String, password: String): Account?
}