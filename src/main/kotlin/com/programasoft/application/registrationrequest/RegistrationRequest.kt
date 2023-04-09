package com.programasoft.application.registrationrequest

import com.programasoft.application.certificate.Certificate
import jakarta.persistence.*
import java.util.*


@Entity
@Table(name = "registration_request")
data class RegistrationRequest(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long,
    @Temporal(TemporalType.DATE) val date: Date = Date(),
    val email: String,
    @Column(name = "full_name") val fullName: String,
    @Column(name = "phone_number") val phoneNumber: String,
    val presentation: String,
    @Column(name = "date_of_approval", nullable = true)
    @Temporal(TemporalType.DATE)
    val dateOfResponse: Date?,
    @Column(name = "status")
    val status: RegistrationStatus = RegistrationStatus.PENDING,
    @OneToMany(mappedBy = "registrationRequest", fetch = FetchType.LAZY)
    var certificates: Set<Certificate> = mutableSetOf()
)

/**
 *
 * {
"date":"2022-03-14",
"email":"john.doe@example.com",
"fullName":"John Doe",
"phoneNumber":"1234567890",
"presentation":"Lorem ipsum dolor sit amet consectetur adipiscing elit",
"status":"PENDING",
"certificates":[
{
"title":"Certificate 1",
"description":"Description for certificate 1",
"image":"ZWVn"
},
{
"title":"Certificate 2",
"description":"Description for certificate 2",
"image":"ZWVn"
}
]
}
 * */