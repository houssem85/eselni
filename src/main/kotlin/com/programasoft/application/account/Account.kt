package com.programasoft.application.account

import jakarta.persistence.*

@Entity
@Table(name = "account")
data class Account(
    @Id @GeneratedValue(strategy = GenerationType.AUTO) var id: Long,
    @Column(unique = true) val email: String = "",
    val password: String = "",
    @Column(name = "full_name")
    val fullName: String = "",
    val connectedDeviceId: String? = null
)