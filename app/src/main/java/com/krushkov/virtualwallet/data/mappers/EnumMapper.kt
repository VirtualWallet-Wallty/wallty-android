package com.krushkov.virtualwallet.data.mappers

import com.krushkov.virtualwallet.domain.models.outputs.card.CardStatus
import com.krushkov.virtualwallet.domain.models.outputs.transaction.TransactionStatus
import com.krushkov.virtualwallet.domain.models.outputs.transaction.TransactionType
import com.krushkov.virtualwallet.domain.models.outputs.user.RoleType

fun String.toRoleType(): RoleType {
    return try {
        RoleType.valueOf(this)
    } catch (e: Exception) {
        RoleType.UNKNOWN
    }
}

fun String.toTransactionStatus(): TransactionStatus {
    return try {
        TransactionStatus.valueOf(this)
    } catch (e: Exception) {
        TransactionStatus.UNKNOWN
    }
}

fun String.toTransactionType(): TransactionType {
    return try {
        TransactionType.valueOf(this)
    } catch (e: Exception) {
        TransactionType.UNKNOWN
    }
}

fun String.toCardStatus(): CardStatus {
    return try {
        CardStatus.valueOf(this)
    } catch (e: Exception) {
        CardStatus.UNKNOWN
    }
}