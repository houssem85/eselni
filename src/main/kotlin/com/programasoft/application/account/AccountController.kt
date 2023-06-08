package com.programasoft.application.account

import com.programasoft.application.accountbalancetransaction.AccountBalanceTransactionService
import com.programasoft.application.administrator.AdministratorService
import com.programasoft.application.psychologist.PsychologistService
import com.programasoft.application.client.ClientService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RequestMapping("api/v1/accounts")
@RestController
class AccountController(
        private val accountService: AccountService,
        private val clientService: ClientService,
        private val psychologistService: PsychologistService,
        private val administratorService: AdministratorService,
        private val accountBalanceTransactionService: AccountBalanceTransactionService
) {

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    fun login(
            @RequestParam(name = "email") login: String, @RequestParam(name = "password") password: String
    ): LoginResponse {
        val account = accountService.getByEmailAndPassword(login, password)
        if (account != null) {
            return LoginResponse(
                    client = clientService.getByAccount(account),
                    psychologist = psychologistService.getByAccount(account),
                    administrator = administratorService.getByAccount(account)
            )
        } else {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "HTTP Status will be CONFLICT (CODE 401)\n")
        }
    }

    @PutMapping("/{id}/connected-device-id")
    fun updateConnectedDeviceId(
            @PathVariable id: Long, @RequestParam connectedDeviceId: String
    ): Account {
        return accountService.updateConnectedDeviceId(id, connectedDeviceId)
    }

    @GetMapping("/{id}/balance")
    fun getCurrentBalance(@PathVariable id: Long): ResponseEntity<Map<String, Double>> {
        val balance = accountBalanceTransactionService.getCurrentBalance(id)
        val creditBalance = accountBalanceTransactionService.getCreditBalance(id)
        val debitBalance = accountBalanceTransactionService.getDebitBalance(id)
        val response = mapOf(
                "balance" to balance,
                "creditBalance" to creditBalance,
                "debitBalance" to debitBalance
        )
        return ResponseEntity.ok(response)
    }

    @PostMapping("/{id}/logout")
    @ResponseStatus(HttpStatus.OK)
    fun logout(
            @PathVariable id: Long
    ) {
        accountService.updateConnectedDeviceId(id, null)
    }
}