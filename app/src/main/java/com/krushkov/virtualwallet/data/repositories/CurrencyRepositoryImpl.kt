package com.krushkov.virtualwallet.data.repositories

import com.krushkov.virtualwallet.data.api.interfaces.CurrencyApi
import com.krushkov.virtualwallet.data.mappers.toDomain
import com.krushkov.virtualwallet.data.remote.ApiHandler.apiCall
import com.krushkov.virtualwallet.domain.models.outputs.currency.Currency
import com.krushkov.virtualwallet.domain.repositories.CurrencyRepository
import com.krushkov.virtualwallet.domain.result.AppResult
import com.krushkov.virtualwallet.domain.result.map
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val api: CurrencyApi
) : CurrencyRepository {
    override suspend fun getAllActive(): AppResult<List<Currency>> {
        return apiCall {
            api.getAllActive()
        }.map { list -> list.map { it.toDomain() } }
    }

    override suspend fun getByCode(code: String): AppResult<Currency> {
        return apiCall {
            api.getByCode(code)
        }.map { it.toDomain() }
    }
}