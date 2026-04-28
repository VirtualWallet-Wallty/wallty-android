package com.krushkov.virtualwallet.data.dtos.request

import java.math.BigDecimal

data class TransferRequest(
    val amount: BigDecimal,
    val currencyCode: String,
    val recipientId: Long,
    val sourceWalletId: Long,
    val targetWalletId: Long? = null
)
