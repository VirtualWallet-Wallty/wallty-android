package com.krushkov.virtualwallet.domain.models.inputs

import java.math.BigDecimal

data class PaymentInput(
    val amount: BigDecimal,
    val merchantReference: String
)