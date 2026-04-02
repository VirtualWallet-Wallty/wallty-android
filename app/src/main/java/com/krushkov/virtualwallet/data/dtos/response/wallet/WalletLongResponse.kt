package com.krushkov.virtualwallet.data.dtos.response.wallet

import com.krushkov.virtualwallet.data.dtos.response.currency.CurrencyShortResponse
import com.krushkov.virtualwallet.data.dtos.response.user.UserShortResponse
import java.math.BigDecimal
import java.time.LocalDateTime

data class WalletLongResponse(
    val id: Long,
    val owner: UserShortResponse,

    val name: String,
    val balance: BigDecimal,
    val currency: CurrencyShortResponse,

    val version: Long,
    val isDefault: Boolean,

    val createdAt: String,
    val updatedAt: String
)