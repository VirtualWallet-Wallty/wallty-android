package com.krushkov.virtualwallet.data.dtos.request

import java.math.BigDecimal

data class TransferRequest(
    val amount: BigDecimal,
    val recipientId: Long
)
