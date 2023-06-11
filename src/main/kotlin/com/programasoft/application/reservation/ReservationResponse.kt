package com.programasoft.application.reservation

data class ReservationResponse(
    val id: Long,
    val psychologist: String,
    val date: String,
    val startTime: String,
    val endTime: String,
    val isPaid: Boolean,
    val amount: Float
)