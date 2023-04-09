package com.programasoft.application.certificate

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*


@RequestMapping("api/v1/certificates")
@RestController
class CertificateController(
    private val certificateService: CertificateService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    fun saveCertificate(@RequestBody certificate: Certificate): Certificate {
        return certificateService.create(certificate)
    }
}