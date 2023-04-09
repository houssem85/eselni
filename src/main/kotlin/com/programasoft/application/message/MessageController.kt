package com.programasoft.application.message

import com.programasoft.application.attachment.AttachmentService
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RequestMapping("api/v1/messages")
@RestController
class MessageController(
    private val messageService: MessageService,
    private val attachmentService: AttachmentService,
) {
    @GetMapping("/conversation")
    fun getConversationByPage(
        @RequestParam("sender_account_id") senderAccountId: Long,
        @RequestParam("receiver_account_id") receiverAccountId: Long,
        @RequestParam("page", defaultValue = "0") pageNumber: Int,
        @RequestParam("size", defaultValue = "10") pageSize: Int
    ): ResponseEntity<Page<Message>> {
        val messages: Page<Message> =
            messageService.getConversationMessagesByPage(senderAccountId, receiverAccountId, pageNumber, pageSize)
                .map { message ->
                    val attachments = attachmentService.findByMessage(message)
                    message.copy(attachments = attachments)
                }
        return ResponseEntity.ok(messages)
    }

    @GetMapping("/conversations")
    fun getLastMessages(
        @RequestParam("account_id") accountId: Long,
        @RequestParam(required = false, defaultValue = "0") page: Int,
        @RequestParam(required = false, defaultValue = "10") size: Int
    ): ResponseEntity<Page<Message>> {
        val messages: Page<Message> = messageService.getLastMessages(accountId, page, size)
            .map { message ->
                val attachments = attachmentService.findByMessage(message)
                message.copy(attachments = attachments)
            }
        return ResponseEntity.ok(messages)
    }
}