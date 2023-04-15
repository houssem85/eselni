package com.programasoft.application.client

import com.programasoft.application.account.Account
import jakarta.persistence.EntityNotFoundException
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

    fun getById(id: Long): Client {
        return clientRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Client with id $id not found") }
    }

}