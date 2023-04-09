package com.programasoft.application.client

import com.programasoft.application.account.Account
import jakarta.persistence.*


@Entity
@Table(name = "client")
data class Client(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long,
    @OneToOne(cascade = [CascadeType.REMOVE], fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    val account: Account
)

/**

{
"id": 1,
"fullName": "John Doe",
"account": {
"id": 1,
"email": "johndoe@example.com",
"password": "********"
}
}
 * */