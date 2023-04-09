package com.programasoft.application.client

import com.programasoft.application.account.Account
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface ClientRepository : JpaRepository<Client, Long> {
    fun findByAccount(account: Account): Client?
}