package com.krushkov.virtualwallet.domain.models.outputs.wallet

import com.krushkov.virtualwallet.domain.models.outputs.currency.Currency
import com.krushkov.virtualwallet.domain.models.outputs.user.UserPreview
import java.math.BigDecimal
import java.time.LocalDateTime

data class Wallet(
    val id: Long,
    val ownerId: Long?,
    val owner: UserPreview?,

    val name: String,
    val balance: BigDecimal,

    val currencyCode: String?,
    val currency: Currency?,

    val isDefault: Boolean,

    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
)