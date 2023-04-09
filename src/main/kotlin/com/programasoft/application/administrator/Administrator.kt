package com.programasoft.application.administrator

import com.programasoft.application.account.Account
import jakarta.persistence.*

@Entity
@Table(name = "administrator")
data class Administrator(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long,

    @OneToOne(cascade = [CascadeType.REMOVE], fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    val account: Account
)