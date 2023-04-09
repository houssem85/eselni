package com.programasoft.application.account

import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*

@Service
class AccountService(
    private val accountRepository: AccountRepository
) {

    fun create(account: Account): Account {
        val passwordHashed = md5(account.password)
        return accountRepository.save(account.copy(password = passwordHashed))
    }

    private fun md5(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }

    fun isEmailExists(email: String): Boolean {
        return accountRepository.existsByEmail(email)
    }

    fun getByEmailAndPassword(email: String, password: String): Account? {
        return accountRepository.findByEmailAndPassword(email, md5(password))
    }

    fun getById(id: Long): Account? {
        return accountRepository.findById(id).orElseThrow { EntityNotFoundException("Account with id $id not found") }
    }

    fun updateConnectedDeviceId(accountId: Long, connectedDeviceId: String?): Account {
        val account = accountRepository.findById(accountId)
            .orElseThrow { EntityNotFoundException("Account with id $accountId not found") }
        return accountRepository.save(
            account.copy(
                connectedDeviceId = connectedDeviceId
            )
        )
    }
}