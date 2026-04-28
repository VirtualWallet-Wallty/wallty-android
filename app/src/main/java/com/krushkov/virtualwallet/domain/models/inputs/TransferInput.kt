package com.krushkov.virtualwallet.domain.models.inputs

import java.math.BigDecimal

data class TransferInput(
    val amount: BigDecimal,
    val currencyCode: String,
    val recipientId: Long,
    val sourceWalletId: Long,
    val targetWalletId: Long? = null
)
