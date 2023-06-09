package com.programasoft.application.message

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Notification
import com.programasoft.application.account.AccountService
import com.programasoft.application.accountbalancetransaction.AccountBalanceTransaction
import com.programasoft.application.accountbalancetransaction.AccountBalanceTransactionService
import com.programasoft.application.accountbalancetransaction.TransactionType
import com.programasoft.application.psychologist.PsychologistService
import com.programasoft.application.attachment.AttachmentService
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller
import java.util.UUID

@Controller
class MessageSocketController(
    private val messageService: MessageService,
    private val accountService: AccountService,
    private val attachmentService: AttachmentService,
    private val simpleMessagingTemplate: SimpMessagingTemplate,
    private val psychologistService: PsychologistService,
    private val accountBalanceTransactionService: AccountBalanceTransactionService,
    private val firebaseMessaging: FirebaseMessaging
) {

    @MessageMapping("/message") //6
    fun sendMessage(message: Message) {
        val psychologist = psychologistService.getByAccount(message.receiverAccount)
        val isReceiverPsychologist= psychologist != null
        val isMessageAllowedToBeSent: Boolean = if (isReceiverPsychologist) {
            val balance = accountBalanceTransactionService.getCurrentBalance(message.senderAccount.id)
            balance - psychologist!!.messageRate >= 0
        } else {
            true
        }
        if (isReceiverPsychologist && isMessageAllowedToBeSent) {
            val groupId = UUID.randomUUID().toString()
            val negativeAccountBalanceTransaction = AccountBalanceTransaction(
                id = 0,
                amount = psychologist!!.messageRate * -1,
                groupId = groupId,
                type = TransactionType.MESSAGE_PAYMENT,
                transactionId = null,
                account = message.senderAccount
            )
            val positiveAccountBalanceTransaction = AccountBalanceTransaction(
                id = 0,
                amount = psychologist.messageRate,
                groupId = groupId,
                type = TransactionType.MESSAGE_PAYMENT,
                transactionId = null,
                account = message.senderAccount
            )
            accountBalanceTransactionService.create(negativeAccountBalanceTransaction)
            accountBalanceTransactionService.create(positiveAccountBalanceTransaction)
            val newBalance = accountBalanceTransactionService.getCurrentBalance(message.senderAccount.id)
            simpleMessagingTemplate.convertAndSendToUser(message.senderAccount.id.toString(), "/balance", newBalance)
        }

        if (isMessageAllowedToBeSent) {
            val message = messageService.create(message)
            message.attachments.forEach {
                attachmentService.create(
                    it.copy(
                        message = message
                    )
                )
            }
            simpleMessagingTemplate.convertAndSendToUser(message.receiverAccount.id.toString(), "/msg", message)
            val receiverAccount = accountService.getById(message.receiverAccount.id)
            val notification = Notification.builder()
                .setTitle(message.receiverAccount.fullName)
                .setBody(message.content)
                .build()
            val notificationMessage = com.google.firebase.messaging.Message.builder()
                .setNotification(notification)
                .setToken(receiverAccount?.connectedDeviceId ?: "")
                .build()
            firebaseMessaging.send(notificationMessage)
        }
    }
}