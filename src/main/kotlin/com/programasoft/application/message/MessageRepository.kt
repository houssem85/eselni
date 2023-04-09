package com.programasoft.application.message

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface MessageRepository : JpaRepository<Message, Long> {
    @Query("SELECT m FROM Message m WHERE (m.senderAccount.id = :senderAccountId AND m.receiverAccount.id = :receiverAccountId) OR (m.senderAccount.id = :receiverAccountId AND m.receiverAccount.id = :senderAccountId)")
    fun findByConversation(senderAccountId: Long, receiverAccountId: Long, pageable: Pageable): Page<Message>

    @Query(
        """
    SELECT m
    FROM Message m
    WHERE (m.senderAccount.id = :accountId OR m.receiverAccount.id = :accountId)
    AND m.date = (
        SELECT MAX(m2.date)
        FROM Message m2
        WHERE m2.senderAccount.id = m.senderAccount.id
        AND m2.receiverAccount.id = m.receiverAccount.id
    )
    ORDER BY m.date DESC
    """
    )
    fun findLastMessagesByAccount(accountId: Long, pageable: Pageable): Page<Message>
}