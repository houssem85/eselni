package com.programasoft.application.availability

import jakarta.persistence.*
import java.time.LocalDateTime


@Entity
@Table(name = "availability")
data class Availability(
    @Id @GeneratedValue(strategy = GenerationType.AUTO) var id: Long,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "availability_group_id", referencedColumnName = "id")
    val availabilityGroup: AvailabilityGroup,
    @Column(name = "start")
    var start: LocalDateTime,
    @Column(name = "end")
    var end: LocalDateTime,
    @OneToMany(mappedBy = "availability", cascade = [CascadeType.ALL], orphanRemoval = true)
    val availabilityUnits: List<AvailabilityUnit> = arrayListOf(),
)