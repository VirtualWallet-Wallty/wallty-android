package com.krushkov.virtualwallet.data.dtos.response.user

import com.krushkov.virtualwallet.data.dtos.response.card.CardShortResponse
import com.krushkov.virtualwallet.data.dtos.response.wallet.WalletShortResponse
import java.time.LocalDateTime

data class UserLongResponse(
    val id: Long,
    val username: String,

    val firstName: String,
    val lastName: String,

    val email: String,
    val phoneNumber: String,
    val photoUrl: String,

    val role: String,

    val isBlocked: Boolean,

    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,

    val wallets: List<WalletShortResponse>,
    val cards: List<CardShortResponse>
)
