package com.krushkov.virtualwallet.domain.models.outputs.exchangerate

import com.krushkov.virtualwallet.domain.models.outputs.currency.Currency
import java.math.BigDecimal
import java.time.LocalDateTime

data class ExchangeRate(
    val id: Long,
    val fromCurrency: Currency,
    val toCurrency: Currency,
    val rate: BigDecimal,
    val lastUpdated: LocalDateTime?
)
