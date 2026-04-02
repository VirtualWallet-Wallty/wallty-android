package com.krushkov.virtualwallet.domain.models.inputs.wallet

data class WalletCreateInput(
    val name: String,
    val currencyCode: String
)