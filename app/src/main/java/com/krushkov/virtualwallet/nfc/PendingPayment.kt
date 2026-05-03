package com.krushkov.virtualwallet.nfc

import java.math.BigDecimal

data class PendingPayment(
    val amount: BigDecimal,
    val currencyCode: String,
    val merchantReference: String
)
