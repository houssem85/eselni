package com.programasoft.application.client

import com.programasoft.application.account.Account
import org.springframework.stereotype.Service


@Service
class ClientService(
    val clientRepository: ClientRepository,
) {
    fun create(
        client: Client
    ): Client {
        return clientRepository.save(client)
    }

    fun getByAccount(account: Account): Client? {
        return clientRepository.findByAccount(account = account)
    }
}