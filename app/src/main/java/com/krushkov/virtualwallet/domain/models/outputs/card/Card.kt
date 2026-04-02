package com.krushkov.virtualwallet.domain.models.outputs.card

import com.krushkov.virtualwallet.domain.models.outputs.user.UserPreview
import java.time.LocalDateTime

data class Card(
    val id: Long,
    val ownerId: Long?,
    val owner: UserPreview?,

    val cardHolder: String?,

    val cardSuffix: String,

    val expirationMonth: Int?,
    val expirationYear: Int?,

    val status: CardStatus,

    val createdAt: LocalDateTime?
)
