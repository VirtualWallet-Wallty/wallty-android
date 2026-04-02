package com.krushkov.virtualwallet.data.mappers

import com.krushkov.virtualwallet.data.dtos.request.card.CardCreateRequest
import com.krushkov.virtualwallet.data.dtos.response.card.CardLongResponse
import com.krushkov.virtualwallet.data.dtos.response.card.CardShortResponse
import com.krushkov.virtualwallet.domain.models.inputs.card.CardCreateInput
import com.krushkov.virtualwallet.domain.models.outputs.card.Card

fun CardShortResponse.toDomain(): Card {
    return Card(
        id = id,
        ownerId = ownerId,
        owner = null,
        cardHolder = null,
        cardSuffix = cardSuffix,
        status = status.toCardStatus(),
        expirationMonth = null,
        expirationYear = null,
        createdAt = null
    )
}

fun CardLongResponse.toDomain(): Card {
    return Card(
        id = id,
        ownerId = null,
        owner = owner.toDomain(),
        cardHolder = cardHolder,
        cardSuffix = cardSuffix,
        status = status.toCardStatus(),
        expirationMonth = expirationMonth,
        expirationYear = expirationYear,
        createdAt = createdAt
    )
}

fun CardCreateInput.toRequest(): CardCreateRequest {
    val (month, year) = expiration.split("/")

    return CardCreateRequest(
        cardHolder = cardHolder,
        cardNumber = cardHolder,
        expirationMonth = month,
        expirationYear = year
    )
}