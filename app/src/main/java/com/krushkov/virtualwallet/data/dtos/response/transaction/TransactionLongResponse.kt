package com.krushkov.virtualwallet.data.dtos.response.transaction

import com.krushkov.virtualwallet.data.dtos.response.currency.CurrencyShortResponse
import com.krushkov.virtualwallet.data.dtos.response.user.UserShortResponse
import com.krushkov.virtualwallet.data.dtos.response.wallet.WalletShortResponse
import java.math.BigDecimal
import java.time.LocalDateTime

data class TransactionLongResponse(
    val id: Long,

    val type: String,
    val status: String,

    val amount: BigDecimal,
    val currency: CurrencyShortResponse,

    val sender: UserShortResponse,
    val recipient: UserShortResponse,

    val senderWallet: WalletShortResponse,
    val recipientWallet: WalletShortResponse,

    val externalReference: String,

    val createdAt: LocalDateTime
)