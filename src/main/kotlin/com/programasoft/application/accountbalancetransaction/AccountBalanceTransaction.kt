package com.programasoft.application.accountbalancetransaction

import com.programasoft.application.account.Account
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "account_balance_transaction")
data class AccountBalanceTransaction(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long,
    @Temporal(TemporalType.TIMESTAMP)
    val date: Date = Date(),
    val amount: Float,
    @Column(name = "group_id")
    val groupId: String?,
    @Enumerated(EnumType.STRING)
    val type: TransactionType,
    val transactionId: String?,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    val account: Account?
)

/**
 * we can add message after
 * */