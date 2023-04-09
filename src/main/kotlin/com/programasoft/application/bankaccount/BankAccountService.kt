package com.programasoft.application.bankaccount

import org.springframework.stereotype.Service

@Service
class BankAccountService(
    private val bankAccountRepository: BankAccountRepository
) {

    fun create(bankAccount: BankAccount): BankAccount {
        return bankAccountRepository.save(bankAccount)
    }
}