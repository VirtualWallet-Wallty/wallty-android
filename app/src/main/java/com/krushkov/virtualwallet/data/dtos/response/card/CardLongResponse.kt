package com.krushkov.virtualwallet.data.dtos.response.card

import com.krushkov.virtualwallet.data.dtos.response.user.UserShortResponse
import java.time.LocalDateTime

data class CardLongResponse(
    val id: Long,
    val owner: UserShortResponse,

    val cardHolder: String,
    val cardSuffix: String,

    val expirationMonth: Int,
    val expirationYear: Int,

    val status: String,

    val createdAt: LocalDateTime
)
