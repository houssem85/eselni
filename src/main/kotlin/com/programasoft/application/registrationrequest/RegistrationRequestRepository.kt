package com.programasoft.application.registrationrequest

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RegistrationRequestRepository : JpaRepository<RegistrationRequest, Long> {
    fun findByStatus(status: RegistrationStatus): List<RegistrationRequest>
}