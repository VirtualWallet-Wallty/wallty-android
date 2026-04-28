package com.krushkov.virtualwallet.data.dtos.request

import java.math.BigDecimal

data class TopUpRequest(
    val walletId: Long,
    val amount: BigDecimal,
    val currencyCode: String,
    val externalReference: String,
    val cardId: Long
)
