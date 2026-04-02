package com.krushkov.virtualwallet.data.dtos.request

import java.math.BigDecimal

data class TopUpRequest(
    val amount: BigDecimal,
    val cardId: Long
)
