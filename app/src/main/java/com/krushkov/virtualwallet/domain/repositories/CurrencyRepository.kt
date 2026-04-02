package com.krushkov.virtualwallet.domain.repositories

import com.krushkov.virtualwallet.domain.models.outputs.currency.Currency
import com.krushkov.virtualwallet.domain.result.AppResult

interface CurrencyRepository {

    suspend fun getAllActive(): AppResult<List<Currency>>

    suspend fun getByCode(code: String): AppResult<Currency>

}