package com.programasoft.application.certificate

import com.programasoft.application.psychologist.Psychologist
import com.programasoft.application.registrationrequest.RegistrationRequest
import org.springframework.data.jpa.repository.JpaRepository

interface CertificateRepository : JpaRepository<Certificate, Long> {
    fun findByRegistrationRequest(registrationRequest: RegistrationRequest): Set<CertificateSummary>

    fun findByPsychologist(psychologist: Psychologist): Set<CertificateSummary>
}