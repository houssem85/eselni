package com.programasoft.application.accountbalancetransaction

import com.programasoft.application.account.Account
import com.programasoft.application.availability.AvailabilityService
import com.programasoft.application.psychologist.PsychologistService
import com.programasoft.application.reservation.ReservationService
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.*

@RequestMapping("api/v1/transactions")
@RestController
class AccountBalanceTransactionController(
    private val accountBalanceTransactionService: AccountBalanceTransactionService,
    private val reservationService: ReservationService,
    private val psychologistService: PsychologistService,
    private val availabilityService: AvailabilityService,
) {

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    fun createTransaction(@RequestBody request: PaymentRequest) {
        val accountBalanceTransaction = AccountBalanceTransaction(
                id = 0,
                amount = request.amount,
                groupId = UUID.randomUUID().toString(),
                type = TransactionType.DEPOSIT,
                transactionId = "${request.transactionId}",
                account = Account(
                        id = request.accountId
                )
        )
        accountBalanceTransactionService.create(accountBalanceTransaction)
    }

    @PostMapping("/payment-reservation")
    @ResponseStatus(HttpStatus.OK)
    fun paymentReservation(
        @RequestParam("reservation_id") reservationId: Long
    ) {
        val reservation = reservationService.getById(reservationId)
        val client = reservation.client
        val availabilityUnits = availabilityService.getAllByReservationId(reservation.id)
        val psychologist = psychologistService.getPsychologistByAvailabilityUnitId(availabilityUnits.first().id)
        val availabilityUnitRate = psychologist.hourlyRate / 2
        val totalAmountToPay = availabilityUnitRate * availabilityUnits.size
        val currentClientBalance = accountBalanceTransactionService.getCurrentBalance(client!!.account.id)
        if (totalAmountToPay <= currentClientBalance) {
            val uuid = UUID.randomUUID().toString()
            val negativeAccountBalanceTransaction = AccountBalanceTransaction(
                id = 0,
                amount = totalAmountToPay * -1,
                groupId = uuid,
                type = TransactionType.RESERVATION_PAYMENT,
                account = client.account,
                transactionId = null,
                reservation = reservation
            )
            val positiveAccountBalanceTransaction = AccountBalanceTransaction(
                id = 0,
                amount = totalAmountToPay,
                groupId = uuid,
                type = TransactionType.RESERVATION_PAYMENT,
                account = psychologist.account,
                transactionId = null,
                reservation = reservation
            )
            accountBalanceTransactionService.create(negativeAccountBalanceTransaction)
            accountBalanceTransactionService.create(positiveAccountBalanceTransaction)
        } else {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "HTTP Status will be UNAUTHORIZED (CODE 401)\n")
        }
    }

    @GetMapping
    fun getHistory(
        @RequestParam("account_id") accountId: Long,
        @RequestParam(required = false, defaultValue = "0") page: Int,
        @RequestParam(required = false, defaultValue = "20") size: Int
    ): ResponseEntity<Page<AccountBalanceTransaction>> {
        val accountBalanceTransactions: Page<AccountBalanceTransaction> =
            accountBalanceTransactionService.getTransactionsForAccount(accountId, page, size)
        return ResponseEntity.ok(accountBalanceTransactions)
    }

    @GetMapping("/recent/{accountId}")
    fun getRecentTransactionsByAccountId(@PathVariable accountId: Long): List<AccountBalanceTransaction> {
        return accountBalanceTransactionService.getRecentTransactionsByAccountId(accountId)
    }
}