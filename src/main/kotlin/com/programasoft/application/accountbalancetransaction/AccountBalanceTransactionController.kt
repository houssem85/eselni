package com.programasoft.application.accountbalancetransaction

import com.programasoft.application.account.Account
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RequestMapping("api/v1/transactions")
@RestController
class AccountBalanceTransactionController(
    private val accountBalanceTransactionService: AccountBalanceTransactionService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    fun createTransaction(@RequestBody request: PaymentRequest) {
        if (request.paymentStatus) {
            val accountBalanceTransaction = AccountBalanceTransaction(
                id = 0,
                amount = request.amount,
                groupId = UUID.randomUUID().toString(),
                type = TransactionType.DEPOSIT,
                transactionId = "${request.transactionId}",
                account = Account(
                    id = request.note.toLong()
                )
            )
            accountBalanceTransactionService.create(accountBalanceTransaction)
        }
    }

    @GetMapping
    fun getLastMessages(
        @RequestParam("account_id") accountId: Long,
        @RequestParam(required = false, defaultValue = "0") page: Int,
        @RequestParam(required = false, defaultValue = "10") size: Int
    ): ResponseEntity<Page<AccountBalanceTransaction>> {
        val accountBalanceTransactions: Page<AccountBalanceTransaction> =
            accountBalanceTransactionService.getTransactionsForAccount(accountId, page, size)
        return ResponseEntity.ok(accountBalanceTransactions)
    }
}