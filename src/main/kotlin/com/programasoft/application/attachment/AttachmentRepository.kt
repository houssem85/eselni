package com.programasoft.application.attachment

import com.programasoft.application.message.Message
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AttachmentRepository : JpaRepository<Attachment, Long> {
    fun findByMessage(message: Message): Set<AttachmentSummary>
}