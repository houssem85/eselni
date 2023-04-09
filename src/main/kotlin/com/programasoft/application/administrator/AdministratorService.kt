package com.programasoft.application.administrator

import com.programasoft.application.account.Account
import org.springframework.stereotype.Service

@Service
class AdministratorService(
    private val repository: AdministratorRepository
) {
    fun create(administrator: Administrator): Administrator {
        return repository.save(administrator)
    }

    fun count(): Long {
        return repository.count()
    }

    fun getByAccount(account: Account): Administrator? {
        return repository.findByAccount(account)
    }
}