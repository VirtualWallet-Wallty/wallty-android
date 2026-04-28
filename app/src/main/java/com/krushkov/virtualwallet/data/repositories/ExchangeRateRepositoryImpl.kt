package com.krushkov.virtualwallet.data.repositories

import com.krushkov.virtualwallet.data.api.interfaces.ExchangeRateApi
import com.krushkov.virtualwallet.data.mappers.toDomain
import com.krushkov.virtualwallet.data.remote.ApiHandler.apiCall
import com.krushkov.virtualwallet.domain.models.outputs.exchangerate.ExchangeRate
import com.krushkov.virtualwallet.domain.repositories.ExchangeRateRepository
import com.krushkov.virtualwallet.domain.result.AppResult
import com.krushkov.virtualwallet.domain.result.map
import java.math.BigDecimal
import javax.inject.Inject

class ExchangeRateRepositoryImpl @Inject constructor(
    private val api: ExchangeRateApi
) : ExchangeRateRepository {
    override suspend fun getAllRates(): AppResult<List<ExchangeRate>> {
        return apiCall {
            api.getAllRates()
        }.map { list -> list.map { it.toDomain() } }
    }

    override suspend fun getRates(baseCurrencyCode: String): AppResult<List<ExchangeRate>> {
        return apiCall {
            api.getRates(baseCurrencyCode)
        }.map { list -> list.map { it.toDomain() } }
    }

    override suspend fun getRate(
        fromCurrencyCode: String,
        toCurrencyCode: String
    ): AppResult<BigDecimal> {
        return apiCall {
            api.getRate(fromCurrencyCode, toCurrencyCode)
        }
    }
}