package com.krushkov.virtualwallet.data.dtos.response.wallet

import java.math.BigDecimal

data class WalletShortResponse(
    val id: Long,
    val ownerId: Long,

    val name: String,
    val balance: BigDecimal,
    val currencyCode: String,

    val isDefault: Boolean
)