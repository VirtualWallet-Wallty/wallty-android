package com.krushkov.virtualwallet.data.mappers

import com.krushkov.virtualwallet.data.dtos.response.currency.CurrencyLongResponse
import com.krushkov.virtualwallet.data.dtos.response.currency.CurrencyShortResponse
import com.krushkov.virtualwallet.domain.models.outputs.currency.Currency

fun CurrencyShortResponse.toDomain(): Currency {
    return Currency(
        code = code,
        name = name,
        symbol = symbol,
        decimals = null,
        isActive = null
    )
}

fun CurrencyLongResponse.toDomain(): Currency {
    return Currency(
        code = code,
        name = name,
        symbol = symbol,
        decimals = decimals,
        isActive = isActive
    )
}