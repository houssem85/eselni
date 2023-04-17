package com.programasoft.application.certificate

import com.programasoft.application.psychologist.Psychologist
import com.programasoft.application.registrationrequest.RegistrationRequest
import org.springframework.stereotype.Service

@Service
class CertificateService(
    private val repository: CertificateRepository
) {

    fun create(certificate: Certificate): Certificate {
        return repository.save(certificate)
    }

    fun findByRegistrationRequest(registrationRequest: RegistrationRequest): Set<Certificate> {
        return repository.findByRegistrationRequest(registrationRequest = registrationRequest).map {
            val certificate = Certificate(
                id = it.id,
                title = it.title,
                description = it.description,
                image = it.image,
                psychologist = null,
                registrationRequest = null
            )
            certificate
        }.toSet()
    }

    fun findByPsychologist(psychologist: Psychologist): Set<Certificate> {
        return repository.findByPsychologist(psychologist).map {
            val certificate = Certificate(
                id = it.id,
                title = it.title,
                description = it.description,
                image = it.image,
                psychologist = null,
                registrationRequest = null
            )
            certificate
        }.toSet()
    }

    fun updateCertificateWithPsychologist(id: Long, psychologist: Psychologist) {
        val certificate = repository.findById(id).orElseThrow()
        repository.save(certificate.copy(psychologist = psychologist))
    }
}