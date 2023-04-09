package com.programasoft.application.accountbalancetransaction

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface AccountBalanceTransactionRepository : JpaRepository<AccountBalanceTransaction, Long> {
    @Query("SELECT SUM(t.amount) FROM AccountBalanceTransaction t WHERE t.account.id = :accountId")
    fun getCurrentBalance(@Param("accountId") accountId: Long): Double?

    fun findByAccountId(accountId: Long, pageable: Pageable): Page<AccountBalanceTransaction>
}