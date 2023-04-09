package com.programasoft.application.accountbalancetransaction

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class AccountBalanceTransactionService(
    private val repository: AccountBalanceTransactionRepository
) {

    fun create(accountBalanceTransaction: AccountBalanceTransaction) {
        repository.save(accountBalanceTransaction)
    }

    fun getCurrentBalance(accountId: Long): Double {
        val balance = repository.getCurrentBalance(accountId) ?: 0.0
        return balance
    }

    fun getTransactionsForAccount(accountId: Long, pageNumber: Int, pageSize: Int): Page<AccountBalanceTransaction> {
        val pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.DESC, "date")
        return repository.findByAccountId(accountId, pageable).map {
            it.copy(
                account = null
            )
        }
    }
}