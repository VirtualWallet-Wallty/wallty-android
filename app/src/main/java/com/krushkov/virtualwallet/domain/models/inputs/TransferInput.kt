package com.krushkov.virtualwallet.domain.models.inputs

import java.math.BigDecimal

data class TransferInput(
    val amount: BigDecimal,
    val recipientId: Long
)
