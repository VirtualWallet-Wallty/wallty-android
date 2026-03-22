package com.krushkov.virtualwallet.data.model.wallet

data class WalletShortResponse(
    val id: Long,
    val ownerId: Long,
    val name: String,
    val balance: Double,
    val currencyCode: String,
    val isDefault: Boolean
)