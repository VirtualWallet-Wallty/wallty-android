package com.krushkov.virtualwallet.data.mappers

import com.krushkov.virtualwallet.data.dtos.response.exchangerate.ExchangeRateResponse
import com.krushkov.virtualwallet.data.utils.toLocalDateTimeOrNull
import com.krushkov.virtualwallet.domain.models.outputs.exchangerate.ExchangeRate

fun ExchangeRateResponse.toDomain(): ExchangeRate {
    return ExchangeRate(
        id = id,
        fromCurrency = fromCurrency.toDomain(),
        toCurrency = toCurrency.toDomain(),
        rate = rate,
        lastUpdated = lastUpdated.toLocalDateTimeOrNull()
    )
}