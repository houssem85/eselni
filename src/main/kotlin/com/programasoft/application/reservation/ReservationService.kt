package com.programasoft.application.reservation

import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service

@Service
class ReservationService(
    private val repository: ReservationRepository
) {

    fun create(reservation: Reservation): Reservation {
        return repository.save(reservation)
    }

    fun getById(id: Long): Reservation {
        return repository.findById(id).orElseThrow { EntityNotFoundException("Reservation with id $id not found") }
    }
}