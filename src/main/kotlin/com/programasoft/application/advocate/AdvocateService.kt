package com.programasoft.application.advocate

import com.programasoft.application.account.Account
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class AdvocateService(
    private val advocateRepository: AdvocateRepository
) {

    fun create(advocate: Advocate): Advocate {
        return advocateRepository.save(advocate)
    }

    fun getByAccount(account: Account): Advocate? {
        return advocateRepository.findByAccount(account = account)
    }

    fun getById(id: Long): Advocate =
        advocateRepository.findByIdOrNull(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

    fun getAll(): List<Advocate> = advocateRepository.findAll()
}