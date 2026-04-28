package com.krushkov.virtualwallet.data.dtos.response.transaction

import java.math.BigDecimal

data class TransactionShortResponse(
    val id: Long,
    val label: String?,

    val type: String,
    val status: String,

    val senderAmount: BigDecimal,
    val senderCurrencyCode: String,

    val recipientAmount: BigDecimal,
    val recipientCurrencyCode: String,

    val direction: String?,

    val senderWalletId: Long?,
    val recipientWalletId: Long?,

    val createdAt: String
)
