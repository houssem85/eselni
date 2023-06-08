package com.programasoft.application.accountbalancetransaction

import com.fasterxml.jackson.annotation.JsonProperty

data class PaymentRequest(
    @JsonProperty("account_id")
    val accountId: Long,
    val amount: Float,
    @JsonProperty("transaction_id")
    val transactionId: Int,
)