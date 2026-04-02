package com.krushkov.virtualwallet.data.dtos.request.wallet

data class WalletCreateRequest(
    val name: String,
    val currencyCode: String
)
