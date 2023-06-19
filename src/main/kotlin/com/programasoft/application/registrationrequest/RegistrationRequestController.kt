package com.programasoft.application.registrationrequest

import com.programasoft.application.account.AccountService
import com.programasoft.application.certificate.CertificateService
import org.springframework.http.HttpStatus
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.*


@RequestMapping("api/v1/registration_requests")
@RestController
class RegistrationRequestController(
    private val registrationRequestService: RegistrationRequestService,
    private val certificateService: CertificateService,
    private val accountService: AccountService,
    private val mainSender: JavaMailSender
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
        @PathVariable id: Long, @RequestParam status: RegistrationStatus
    ): RegistrationRequest {
        val registrationRequest = registrationRequestService.getById(id)
        val updatedRegistrationRequest = registrationRequestService.create(
            registrationRequest.copy(
                status = status, dateOfResponse = Date()
            )
        )
        when (status) {
            RegistrationStatus.PENDING -> {

            }

            RegistrationStatus.APPROVED -> {
                val subject = "Registration Request Accepted"
                val body = generatePositiveResponseEmail(
                    "http://localhost:3000/create-psychologist/${updatedRegistrationRequest.id}"
                )
                sendEmail(updatedRegistrationRequest.email, subject, body, mainSender)
            }

            RegistrationStatus.REJECTED -> {
                val subject = "Registration Request Rejected"
                val body = generateNegativeResponseEmail(
                )
                sendEmail(updatedRegistrationRequest.email, subject, body, mainSender)
            }
        }
        return updatedRegistrationRequest
    }

    fun sendEmail(to: String, subject: String, body: String, mailSender: JavaMailSender) {
        val message = SimpleMailMessage().apply {
            setTo(to)
            setSubject(subject)
            setText(body)
        }

        mailSender.send(message)
    }

    fun generateNegativeResponseEmail(): String {
        val subject = "Registration Request Rejected"
        val body = """
        Dear Psychologist,
        
        We regret to inform you that your registration request has been rejected. If you have any questions or would like to obtain further information, please feel free to contact us.
        
        Thank you for your interest.
        
        Best regards,
        Administrator
    """.trimIndent()

        return body
    }

    fun generatePositiveResponseEmail(accountCreationLink: String): String {
        val body = """
        Dear Psychologist,
        
        Your registration request has been accepted. You can now create your account using the following link: $accountCreationLink
        
        Thank you and welcome to our online psychological consultation platform.
        
        Best regards,
        Administrator
    """.trimIndent()

        return body
    }
}