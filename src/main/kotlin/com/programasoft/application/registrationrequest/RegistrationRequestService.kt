package com.programasoft.application.registrationrequest

import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class RegistrationRequestService(
    private val registrationRequestRepository: RegistrationRequestRepository
) {
    fun create(registrationRequest: RegistrationRequest): RegistrationRequest {
        return registrationRequestRepository.save(registrationRequest)
    }

    fun getById(id: Long): RegistrationRequest = registrationRequestRepository.findByIdOrNull(id) ?:
    throw ResponseStatusException(HttpStatus.NOT_FOUND)

    fun getRegistrationRequestsByStatus(status: RegistrationStatus): List<RegistrationRequest> {
        return registrationRequestRepository.findByStatus(status)
    }
}