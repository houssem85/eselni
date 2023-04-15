package com.programasoft.application.reservation

import com.programasoft.application.availability.AvailabilityService
import com.programasoft.application.certificate.Certificate
import com.programasoft.application.client.Client
import com.programasoft.application.client.ClientService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RequestMapping("api/v1/reservations")
@RestController
class ReservationController(
    private val availabilityService: AvailabilityService,
    private val reservationService: ReservationService,
    private val clientService: ClientService,
) {

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    fun saveReservation(@RequestBody request: CreateReservationRequest) {
        val client = clientService.getById(request.clientId)
        val reservation = Reservation(
            id = 0,
            client = client
        )
        val newReservation = reservationService.create(reservation)
        request.availabilityUnitIds.forEach {
            availabilityService.updateAvailabilityUnitReservation(it, newReservation)
        }
    }
}