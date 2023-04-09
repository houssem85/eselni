package com.programasoft.application.administrator

import com.programasoft.application.account.Account
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AdministratorRepository : JpaRepository<Administrator, Long> {
    fun findByAccount(account: Account): Administrator?
}