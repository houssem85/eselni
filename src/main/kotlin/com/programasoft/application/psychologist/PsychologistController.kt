package com.programasoft.application.psychologist

import com.programasoft.application.account.AccountService
import com.programasoft.application.bankaccount.BankAccountService
import com.programasoft.application.certificate.CertificateService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RequestMapping("api/v1/psychologists")
@RestController
class PsychologistController(
    private val psychologistService: PsychologistService,
    private val accountService: AccountService,
    private val bankAccountService: BankAccountService,
    private val certificateService: CertificateService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    fun savePsychologist(@RequestBody psychologist: Psychologist): Psychologist? {
        val account = accountService.create(psychologist.account)
        val bankAccount = bankAccountService.create(psychologist.bankAccount)
        val newPsychologist = psychologistService.create(psychologist.copy(account = account, bankAccount = bankAccount))
        psychologist.certificates.forEach {
            certificateService.updateCertificateWithPsychologist(it.id, newPsychologist)
        }
        return psychologist
    }

    @GetMapping("/{id}")
    fun getPsychologist(@PathVariable id: Long): Psychologist {
        val psychologist = psychologistService.getById(id)
        return psychologist.copy(
            certificates = certificateService.findByPsychologist(psychologist)
        )
    }

    @GetMapping
    fun getAllPsychologists() = psychologistService.getAll().map {
        it.copy(
            certificates = certificateService.findByPsychologist(it)
        )
    }
}