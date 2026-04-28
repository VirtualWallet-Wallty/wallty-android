package com.krushkov.virtualwallet.domain.models.outputs.transaction

import com.krushkov.virtualwallet.domain.models.outputs.currency.Currency
import com.krushkov.virtualwallet.domain.models.outputs.user.UserPreview
import com.krushkov.virtualwallet.domain.models.outputs.wallet.Wallet
import java.math.BigDecimal
import java.time.LocalDateTime

data class Transaction(
    val id: Long,
    val label: String?,

    val type: TransactionType,
    val status: TransactionStatus,

    val senderAmount: BigDecimal,
    val senderCurrency: Currency?,
    val senderCurrencyCode: String?,

    val recipientAmount: BigDecimal,
    val recipientCurrency: Currency?,
    val recipientCurrencyCode: String?,

    val sender: UserPreview?,
    val recipient: UserPreview?,

    val senderWallet: Wallet?,
    val recipientWallet: Wallet?,
    val senderWalletId: Long?,
    val recipientWalletId: Long?,

    val externalReference: String?,

    val direction: String?,

    val createdAt: LocalDateTime?
)