package com.krushkov.virtualwallet.domain.repositories

import com.krushkov.virtualwallet.domain.models.outputs.exchangerate.ExchangeRate
import com.krushkov.virtualwallet.domain.result.AppResult
import java.math.BigDecimal

interface ExchangeRateRepository {

    suspend fun getAllRates(): AppResult<List<ExchangeRate>>

    suspend fun getRates(baseCurrencyCode: String): AppResult<List<ExchangeRate>>

    suspend fun getRate(
        fromCurrencyCode: String,
        toCurrencyCode: String
    ): AppResult<BigDecimal>
}