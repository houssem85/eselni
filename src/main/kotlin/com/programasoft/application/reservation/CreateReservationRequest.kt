package com.programasoft.application.reservation

data class CreateReservationRequest(
    val clientId: Long,
    val availabilityUnitIds: Set<Long>,
)