package com.programasoft.application.advocate

import com.programasoft.application.account.AccountService
import com.programasoft.application.bankaccount.BankAccountService
import com.programasoft.application.certificate.CertificateService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RequestMapping("api/v1/advocates")
@RestController
class AdvocateController(
    private val advocateService: AdvocateService,
    private val accountService: AccountService,
    private val bankAccountService: BankAccountService,
    private val certificateService: CertificateService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    fun saveAdvocate(@RequestBody advocate: Advocate): Advocate? {
        val account = accountService.create(advocate.account)
        val bankAccount = bankAccountService.create(advocate.bankAccount)
        val newAdvocate = advocateService.create(advocate.copy(account = account, bankAccount = bankAccount))
        advocate.certificates.forEach {
            certificateService.updateCertificateWithAdvocate(it.id, newAdvocate)
        }
        return advocate
    }

    @GetMapping("/{id}")
    fun getAdvocate(@PathVariable id: Long): Advocate {
        val advocate = advocateService.getById(id)
        return advocate.copy(
            certificates = certificateService.findByAdvocate(advocate)
        )
    }

    @GetMapping
    fun getAllAdvocates() = advocateService.getAll().map {
        it.copy(
            certificates = certificateService.findByAdvocate(it)
        )
    }
}