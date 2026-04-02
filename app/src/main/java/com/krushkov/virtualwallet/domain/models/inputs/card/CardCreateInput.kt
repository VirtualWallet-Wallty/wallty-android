package com.krushkov.virtualwallet.domain.models.inputs.card

data class CardCreateInput(
    val cardHolder: String,
    val cardNumber: String,
    val expiration: String
)
