package com.programasoft.application.availability

import com.programasoft.application.advocate.Advocate
import jakarta.persistence.*
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate
import java.util.*

@Entity
@Table(name = "availability_group")
data class AvailabilityGroup(
    @Id @GeneratedValue(strategy = GenerationType.AUTO) var id: Long,
    @Column(name = "start_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    val startDate: LocalDate,
    @Column(name = "end_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    val endDate: LocalDate,
    @OneToMany(mappedBy = "availabilityGroup", cascade = [CascadeType.ALL], orphanRemoval = true)
    val availabilities: List<Availability> = emptyList(),
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "advocate_id", referencedColumnName = "id")
    val advocate: Advocate
)