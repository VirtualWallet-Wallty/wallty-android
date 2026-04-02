package com.krushkov.virtualwallet.data.dtos.response.transaction

import java.math.BigDecimal

data class TransactionShortResponse(
    val id: Long,

    val type: String,
    val status: String,

    val amount: BigDecimal,
    val currencyCode: String,

    val senderWalletId: Long,
    val recipientWalletId: Long
)