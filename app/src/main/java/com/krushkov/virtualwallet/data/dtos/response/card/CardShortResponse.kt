package com.krushkov.virtualwallet.data.dtos.response.card
data class CardShortResponse (
    val id: Long,
    val ownerId: Long,

    val cardSuffix: String,
    val status: String
)