package com.programasoft.application.accountbalancetransaction

import com.fasterxml.jackson.annotation.JsonProperty

data class PaymentRequest(
    @JsonProperty("token")
    val token: String,
    @JsonProperty("check_sum")
    val checkSum: String,
    @JsonProperty("payment_status")
    val paymentStatus: Boolean,
    @JsonProperty("order_id")
    val orderId: String,
    @JsonProperty("first_name")
    val firstName: String,
    @JsonProperty("last_name")
    val lastName: String,
    val email: String,
    val phone: String,
    val note: String,
    val amount: Float,
    @JsonProperty("transaction_id")
    val transactionId: Int,
    @JsonProperty("received_amount")
    val receivedAmount: Double,
    val cost: Int
)