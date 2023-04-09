package com.programasoft.application.message

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class MessageService(
    private val repository: MessageRepository
) {
    fun create(message: Message): Message {
        return repository.save(message)
    }

    fun getConversationMessagesByPage(
        senderAccountId: Long,
        receiverAccountId: Long,
        pageNumber: Int,
        pageSize: Int
    ): Page<Message> {
        val pageable: Pageable = PageRequest.of(
            pageNumber, pageSize,
            Sort.by(
                "date"
            ).descending()
        )
        return repository.findByConversation(senderAccountId, receiverAccountId, pageable)
    }


    fun getLastMessages(accountId: Long, page: Int, size: Int): Page<Message> {
        val pageable = PageRequest.of(page, size, Sort.by("date").descending())
        return repository.findLastMessagesByAccount(accountId, pageable)
    }
}