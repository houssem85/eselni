package com.programasoft.application.bankaccount

import jakarta.persistence.*


@Entity
@Table(name = "bank_account")
data class BankAccount(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long,
    val IBAN: String,
    val bank: String,
    @Column(name = "full_name")
    val fullName: String
)