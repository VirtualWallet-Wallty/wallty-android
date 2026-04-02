package com.krushkov.virtualwallet.data.dtos.request

import java.math.BigDecimal

data class PaymentRequest(
    val amount: BigDecimal,
    val merchantReference: String
)
