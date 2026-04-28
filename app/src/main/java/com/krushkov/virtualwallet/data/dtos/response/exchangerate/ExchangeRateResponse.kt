package com.krushkov.virtualwallet.data.dtos.response.exchangerate

import com.krushkov.virtualwallet.data.dtos.response.currency.CurrencyShortResponse
import java.math.BigDecimal

data class ExchangeRateResponse(
    val id: Long,
    val fromCurrency: CurrencyShortResponse,
    val toCurrency: CurrencyShortResponse,
    val rate: BigDecimal,
    val lastUpdated: String
)
