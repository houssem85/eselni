package com.programasoft.application.certificate

import com.programasoft.application.advocate.Advocate
import com.programasoft.application.registrationrequest.RegistrationRequest
import org.springframework.data.jpa.repository.JpaRepository

interface CertificateRepository : JpaRepository<Certificate, Long> {
    fun findByRegistrationRequest(registrationRequest: RegistrationRequest): Set<CertificateSummary>

    fun findByAdvocate(advocate: Advocate): Set<CertificateSummary>
}