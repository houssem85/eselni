package com.programasoft.application.client

import com.programasoft.application.account.AccountService
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.sql.SQLIntegrityConstraintViolationException


@RequestMapping("api/v1/clients")
@RestController
class ClientController(
    private val clientService: ClientService,
    private val accountService: AccountService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    fun saveClient(@RequestBody client: Client): Client {
        try {
            val createdAccount = accountService.create(client.account)
            val newClient = client.copy(account = createdAccount)
            return clientService.create(newClient)
        } catch (ex: DataIntegrityViolationException) {
            throw ResponseStatusException(HttpStatus.CONFLICT, "HTTP Status will be CONFLICT (CODE 409)\n")
        }
    }
}