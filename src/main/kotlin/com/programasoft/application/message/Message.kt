package com.programasoft.application.message

import com.programasoft.application.account.Account
import com.programasoft.application.attachment.Attachment
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "message")
data class Message(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long,
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    val date: Date = Date(),
    val content: String?,
    @OneToMany(mappedBy = "message", fetch = FetchType.LAZY, cascade = [CascadeType.REMOVE], orphanRemoval = true)
    val attachments: Set<Attachment> = mutableSetOf(),
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_account_id")
    val senderAccount: Account,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_account_id")
    val receiverAccount: Account
)