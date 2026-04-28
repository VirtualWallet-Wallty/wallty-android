package com.krushkov.virtualwallet.data.mappers

import com.krushkov.virtualwallet.data.dtos.request.PaymentRequest
import com.krushkov.virtualwallet.data.dtos.request.TopUpRequest
import com.krushkov.virtualwallet.data.dtos.request.TransferRequest
import com.krushkov.virtualwallet.data.dtos.response.transaction.TransactionLongResponse
import com.krushkov.virtualwallet.data.dtos.response.transaction.TransactionShortResponse
import com.krushkov.virtualwallet.data.utils.toLocalDateTimeOrNull
import com.krushkov.virtualwallet.domain.models.inputs.PaymentInput
import com.krushkov.virtualwallet.domain.models.inputs.TopUpInput
import com.krushkov.virtualwallet.domain.models.inputs.TransferInput
import com.krushkov.virtualwallet.domain.models.outputs.transaction.Transaction


fun TransactionShortResponse.toDomain(): Transaction {
    return Transaction(
        id = id,
        label = label,
        type = type.toTransactionType(),
        status = status.toTransactionStatus(),
        senderAmount = senderAmount,
        senderCurrency = null,
        senderCurrencyCode = senderCurrencyCode,
        recipientAmount = recipientAmount,
        recipientCurrency = null,
        recipientCurrencyCode = recipientCurrencyCode,
        sender = null,
        recipient = null,
        senderWallet = null,
        recipientWallet = null,
        externalReference = null,
        senderWalletId = senderWalletId,
        recipientWalletId = recipientWalletId,
        direction = direction,
        createdAt = createdAt.toLocalDateTimeOrNull()
    )
}

fun TransactionLongResponse.toDomain(): Transaction {
    return Transaction(
        id = id,
        label = label,
        type = type.toTransactionType(),
        status = status.toTransactionStatus(),
        senderAmount = senderAmount,
        senderCurrency = senderCurrency?.toDomain(),
        senderCurrencyCode = null,
        recipientAmount = recipientAmount,
        recipientCurrency = recipientCurrency?.toDomain(),
        recipientCurrencyCode = null,
        sender = sender?.toDomain(),
        recipient = recipient?.toDomain(),
        senderWallet = senderWallet?.toDomain(),
        recipientWallet = recipientWallet?.toDomain(),
        senderWalletId = senderWallet?.id,
        recipientWalletId = recipientWallet?.id,
        externalReference = externalReference,
        direction = direction,
        createdAt = createdAt.toLocalDateTimeOrNull()
    )
}

fun PaymentInput.toRequest(): PaymentRequest {
    return PaymentRequest(
        amount = amount,
        currencyCode = currencyCode,
        merchantReference = merchantReference,
        sourceWalletId = sourceWalletId
    )
}

fun TopUpInput.toRequest(): TopUpRequest {
    return TopUpRequest(
        walletId = walletId,
        amount = amount,
        currencyCode = currencyCode,
        externalReference = externalReference,
        cardId = cardId
    )
}

fun TransferInput.toRequest(): TransferRequest {
    return TransferRequest(
        amount = amount,
        currencyCode = currencyCode,
        recipientId = recipientId,
        sourceWalletId = sourceWalletId,
        targetWalletId = targetWalletId
    )
}
