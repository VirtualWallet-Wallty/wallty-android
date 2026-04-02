package com.krushkov.virtualwallet.ui.utils

import com.krushkov.virtualwallet.domain.models.outputs.transaction.Transaction
import java.time.format.DateTimeFormatter

fun Transaction.getTitle(currentWalletId: Long): String {

    if (!externalReference.isNullOrBlank()) {
        return externalReference
    }

    val isIncoming = recipientWalletId == currentWalletId
    return if (isIncoming) "Income" else "Outcome"
}

fun Transaction.getFormattedDate(): String {
    return createdAt
        ?.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
        ?: "Unknown date"
}