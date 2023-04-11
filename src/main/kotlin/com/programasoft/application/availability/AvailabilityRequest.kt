package com.programasoft.application.availability

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate


data class AvailabilityRequest(
    @JsonProperty("start_date")
    val startDate: LocalDate,
    @JsonProperty("end_date")
    val endDate: LocalDate,
    val days: Map<String, List<TimeInterval>>,
    @JsonProperty("advocate_id")
    val advocateId: Long,
)

data class TimeInterval(
    val start: String,
    val end: String
)