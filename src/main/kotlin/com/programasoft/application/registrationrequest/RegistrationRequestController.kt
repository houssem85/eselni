package com.programasoft.application.registrationrequest

import com.programasoft.application.account.AccountService
import com.programasoft.application.certificate.CertificateService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.*


@RequestMapping("api/v1/registration_requests")
@RestController
class RegistrationRequestController(
    private val registrationRequestService: RegistrationRequestService,
    private val certificateService: CertificateService,
    private val accountService: AccountService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    fun saveRegistrationRequest(@RequestBody registrationRequest: RegistrationRequest): RegistrationRequest {
        if (!accountService.isEmailExists(registrationRequest.email)) {
            val registrationRequest = registrationRequestService.create(registrationRequest)
            registrationRequest.certificates.forEach {
                val certificate = it.copy(registrationRequest = registrationRequest)
                certificateService.create(certificate)
            }
            return registrationRequest
        } else {
            throw ResponseStatusException(HttpStatus.CONFLICT, "HTTP Status will be CONFLICT (CODE 409)\n")
        }
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getRegistrationRequest(@PathVariable id: Long): RegistrationRequest {
        val registrationRequest = registrationRequestService.getById(id)
        return registrationRequest.copy(
            certificates = certificateService.findByRegistrationRequest(registrationRequest)
        )
    }

    @GetMapping("/status/{status}")
    @ResponseStatus(HttpStatus.OK)
    fun getRegistrationRequestsByStatus(@PathVariable status: RegistrationStatus): List<RegistrationRequest> {
        val registrationRequests = registrationRequestService.getRegistrationRequestsByStatus(status).map {
            val certificates = certificateService.findByRegistrationRequest(it)
            it.copy(
                certificates = certificates
            )
        }
        return registrationRequests
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun updateRegistrationRequestStatus(
        @PathVariable id: Long,
        @RequestParam status: RegistrationStatus
    ): RegistrationRequest {
        val registrationRequest = registrationRequestService.getById(id)
        return registrationRequestService.create(registrationRequest.copy(
            status = status,
            dateOfResponse = Date()
        ))
    }
}