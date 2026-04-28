package com.krushkov.virtualwallet.data.mappers

import com.krushkov.virtualwallet.data.dtos.request.wallet.WalletCreateRequest
import com.krushkov.virtualwallet.data.dtos.response.wallet.WalletLongResponse
import com.krushkov.virtualwallet.data.dtos.response.wallet.WalletShortResponse
import com.krushkov.virtualwallet.data.utils.toLocalDateTimeOrNull
import com.krushkov.virtualwallet.domain.models.inputs.wallet.WalletCreateInput
import com.krushkov.virtualwallet.domain.models.outputs.wallet.Wallet

fun WalletShortResponse.toDomain(): Wallet {
    return Wallet(
        id = id,
        ownerId = ownerId,
        owner = null,
        name = name,
        balance = balance,
        currencyCode = currencyCode,
        currency = null,
        isDefault = isDefault,
        createdAt = null,
        updatedAt = null
    )
}

fun WalletLongResponse.toDomain(): Wallet {
    return Wallet(
        id = id,
        ownerId = owner.id,
        owner = owner.toDomain(),
        name = name,
        balance = balance,
        currencyCode = currency.code,
        currency = currency.toDomain(),
        isDefault = isDefault,
        createdAt = createdAt.toLocalDateTimeOrNull(),
        updatedAt = updatedAt.toLocalDateTimeOrNull()
    )
}

fun WalletCreateInput.toRequest(): WalletCreateRequest {
    return WalletCreateRequest(
        name = name,
        currencyCode = currencyCode
    )
}