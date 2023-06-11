package com.programasoft.application.reservation

import com.programasoft.application.availability.AvailabilityUnit
import com.programasoft.application.client.Client
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "reservation")
data class Reservation(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    val client: Client?,
    @Column(name = "date")
    var date: LocalDateTime = LocalDateTime.now(),
    @OneToMany(mappedBy = "reservation", cascade = [CascadeType.ALL], orphanRemoval = true)
    var availabilityUnits: Set<AvailabilityUnit> = emptySet()
)