package com.programasoft.application.attachment

import com.programasoft.application.message.Message
import jakarta.persistence.*


@Entity
@Table(name = "attachment")
data class Attachment(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long,
    @Column(nullable = false)
    val name: String,
    @Column(nullable = false)
    val type: String,
    @Lob
    @Column(name = "content", columnDefinition = "LONGBLOB")
    val content: ByteArray,
    @ManyToOne(fetch = FetchType.LAZY , cascade = [CascadeType.REMOVE])
    @JoinColumn(name = "message_id")
    val message: Message?
)