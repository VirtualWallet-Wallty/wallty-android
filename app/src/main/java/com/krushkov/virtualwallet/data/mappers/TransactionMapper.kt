package com.krushkov.virtualwallet.data.mappers

import com.krushkov.virtualwallet.data.dtos.request.PaymentRequest
import com.krushkov.virtualwallet.data.dtos.request.TopUpRequest
import com.krushkov.virtualwallet.data.dtos.request.TransferRequest
import com.krushkov.virtualwallet.data.dtos.response.transaction.TransactionLongResponse
import com.krushkov.virtualwallet.data.dtos.response.transaction.TransactionShortResponse
import com.krushkov.virtualwallet.domain.models.inputs.PaymentInput
import com.krushkov.virtualwallet.domain.models.inputs.TopUpInput
import com.krushkov.virtualwallet.domain.models.inputs.TransferInput
import com.krushkov.virtualwallet.domain.models.inputs.transaction.TransactionFilterInput
import com.krushkov.virtualwallet.domain.models.outputs.transaction.Transaction
import com.krushkov.virtualwallet.domain.result.AppResult

fun TransactionShortResponse.toDomain(): Transaction {
    return Transaction(
        id = id,
        type = type.toTransactionType(),
        status = status.toTransactionStatus(),
        amount = amount,
        currencyCode = currencyCode,
        currency = null,
        sender = null,
        recipient = null,
        senderWallet = null,
        recipientWallet = null,
        senderWalletId = senderWalletId,
        recipientWalletId = recipientWalletId,
        externalReference = null,
        createdAt = null
    )
}

fun TransactionLongResponse.toDomain(): Transaction {
    return Transaction(
        id = id,
        type = type.toTransactionType(),
        status = status.toTransactionStatus(),
        amount = amount,
        currencyCode = null,
        currency = currency.toDomain(),
        sender = sender.toDomain(),
        recipient = recipient.toDomain(),
        senderWallet = senderWallet.toDomain(),
        recipientWallet = recipientWallet.toDomain(),
        senderWalletId = null,
        recipientWalletId = null,
        externalReference = externalReference,
        createdAt = createdAt
    )
}

fun PaymentInput.toRequest(): PaymentRequest {
    return PaymentRequest(
        amount = amount,
        merchantReference = merchantReference
    )
}

fun TopUpInput.toRequest(): TopUpRequest {
    return TopUpRequest(
        amount = amount,
        cardId = cardId
    )
}

fun TransferInput.toRequest(): TransferRequest {
    return TransferRequest(
        amount = amount,
        recipientId = recipientId
    )
}