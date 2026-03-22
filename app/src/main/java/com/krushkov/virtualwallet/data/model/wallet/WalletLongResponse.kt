package com.krushkov.virtualwallet.data.model.wallet

import com.krushkov.virtualwallet.data.model.currency.CurrencyShortResponse
import com.krushkov.virtualwallet.data.model.user.UserShortResponse

data class WalletLongResponse(
    val id: Long,
    val owner: UserShortResponse,
    val name: String,
    val balance: Double,
    val currency: CurrencyShortResponse,
    val version: Long,
    val isDefault: Boolean,
    val createdAt: String,
    val updatedAt: String
)