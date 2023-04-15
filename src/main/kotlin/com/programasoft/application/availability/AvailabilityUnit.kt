package com.programasoft.application.availability

import com.programasoft.application.reservation.Reservation
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "availability_unit")
data class AvailabilityUnit(
    @Id @GeneratedValue(strategy = GenerationType.AUTO) var id: Long,
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(
        name = "availability_id",
        referencedColumnName = "id"
    ) val availability: Availability?,
    @Column(name = "start")
    var start: LocalDateTime,
    @Column(name = "end")
    var end: LocalDateTime,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", referencedColumnName = "id")
    var reservation: Reservation? = null
)