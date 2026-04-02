package com.krushkov.virtualwallet.domain.models.outputs.currency

data class Currency(
    val code: String,
    val name: String,
    val symbol: String,

    val decimals: Int?,
    val isActive: Boolean?
)