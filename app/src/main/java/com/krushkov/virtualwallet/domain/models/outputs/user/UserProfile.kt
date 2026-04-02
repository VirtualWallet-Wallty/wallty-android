package com.krushkov.virtualwallet.domain.models.outputs.user

import com.krushkov.virtualwallet.domain.models.outputs.card.Card
import com.krushkov.virtualwallet.domain.models.outputs.wallet.Wallet
import java.time.LocalDateTime

data class UserProfile(
    val id: Long,
    val username: String,

    val firstName: String,
    val lastName: String,

    val email: String,
    val phoneNumber: String,
    val photoUrl: String?,

    val role: RoleType,
    val isBlocked: Boolean,

    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,

    val wallets: List<Wallet>,
    val cards: List<Card>
)