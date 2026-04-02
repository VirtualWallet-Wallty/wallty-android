package com.krushkov.virtualwallet.data.dtos.response.currency

data class CurrencyLongResponse (
    val code: String,

    val name: String,
    val symbol: String,

    val decimals: Int,
    val isActive: Boolean
)