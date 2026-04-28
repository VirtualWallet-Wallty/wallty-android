package com.krushkov.virtualwallet.domain.models.inputs

import java.math.BigDecimal

data class TopUpInput(
    val walletId: Long,
    val amount: BigDecimal,
    val currencyCode: String,
    val externalReference: String,
    val cardId: Long
)
