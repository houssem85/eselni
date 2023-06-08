package com.programasoft.application.availability

import com.programasoft.application.psychologist.Psychologist
import jakarta.persistence.*
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

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
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "psychologist_id", referencedColumnName = "id")
    val psychologist: Psychologist?
)