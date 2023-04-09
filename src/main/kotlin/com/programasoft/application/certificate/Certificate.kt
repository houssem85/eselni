package com.programasoft.application.certificate

import com.fasterxml.jackson.annotation.JsonIgnore
import com.programasoft.application.advocate.Advocate
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
    @JoinColumn(name = "advocate_id")
    val advocate: Advocate?
)