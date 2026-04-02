package com.krushkov.virtualwallet.domain.models.inputs

import java.math.BigDecimal

data class TopUpInput(
    val amount: BigDecimal,
    val cardId: Long
)
