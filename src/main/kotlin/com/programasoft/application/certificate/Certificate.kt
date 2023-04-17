package com.programasoft.application.certificate

import com.programasoft.application.psychologist.Psychologist
import com.programasoft.application.registrationrequest.RegistrationRequest
import jakarta.persistence.*

@Entity
@Table(name = "certificate")
data class Certificate(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long,
    val title: String,
    val description: String,
    @Lob
    @Column(name = "image", columnDefinition = "LONGBLOB")
    val image: ByteArray,
    @ManyToOne
    @JoinColumn(name = "registration_request_id")
    val registrationRequest: RegistrationRequest?,
    @ManyToOne
    @JoinColumn(name = "psychologist_id")
    val psychologist: Psychologist?
)