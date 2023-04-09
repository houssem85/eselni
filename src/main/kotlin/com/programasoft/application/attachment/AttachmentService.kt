package com.programasoft.application.attachment

import com.programasoft.application.message.Message
import org.springframework.stereotype.Service

@Service
class AttachmentService(
    private val repository: AttachmentRepository
) {
    fun create(attachment: Attachment): Attachment {
        return repository.save(attachment)
    }

    fun findByMessage(message: Message): Set<Attachment> {
        return repository.findByMessage(message = message).map {
            Attachment(
                id = it.id,
                name = it.name,
                type = it.type,
                content = it.content,
                message = null
            )
        }.toSet()
    }
}