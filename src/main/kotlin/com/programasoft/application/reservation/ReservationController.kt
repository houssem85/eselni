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

    @GetMapping("client/{clientId}")
    @ResponseStatus(HttpStatus.OK)
    fun getReservationsByClient(
        @PathVariable("clientId") clientId: Long,
        @RequestParam isPaid: Boolean = true
    ): List<Reservation> {
        val list: List<Reservation> = if (isPaid) {
            reservationService.getPaidReservationsByClient(clientId)
        } else {
            reservationService.getNotPaidReservationsByClient(clientId)
        }
        return list.map {
            it.copy(
                availabilityUnits = availabilityService.getAvailabilityUnitByReservation(it.id).toSet()
            )
        }
    }
}