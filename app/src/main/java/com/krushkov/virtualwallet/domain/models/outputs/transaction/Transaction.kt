package com.krushkov.virtualwallet.domain.models.outputs.transaction

import com.krushkov.virtualwallet.domain.models.outputs.currency.Currency
import com.krushkov.virtualwallet.domain.models.outputs.user.UserPreview
import com.krushkov.virtualwallet.domain.models.outputs.wallet.Wallet
import java.math.BigDecimal
import java.time.LocalDateTime

data class Transaction(
    val id: Long,

    val type: TransactionType,
    val status: TransactionStatus,

    val amount: BigDecimal,
    val currencyCode: String?,
    val currency: Currency?,

    val sender: UserPreview?,
    val recipient: UserPreview?,

    val senderWallet: Wallet?,
    val recipientWallet: Wallet?,

    val senderWalletId: Long?,
    val recipientWalletId: Long?,

    val externalReference: String?,

    val createdAt: LocalDateTime?
)