package com.krushkov.virtualwallet.data.dtos.request.card

data class CardCreateRequest (
    val cardHolder: String,
    val cardNumber: String,
    val expirationMonth: String,
    val expirationYear: String
)