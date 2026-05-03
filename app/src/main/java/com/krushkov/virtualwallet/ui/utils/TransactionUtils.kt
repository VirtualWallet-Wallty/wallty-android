package com.krushkov.virtualwallet.ui.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.krushkov.virtualwallet.domain.models.outputs.transaction.Transaction
import com.krushkov.virtualwallet.ui.theme.Green
import com.krushkov.virtualwallet.ui.theme.Orange
import com.krushkov.virtualwallet.ui.theme.Red
import java.time.format.DateTimeFormatter

fun Transaction.isIncoming(currentWalletId: Long?, ownedWalletIds: Set<Long> = emptySet()): Boolean {
    if (currentWalletId != null) return recipientWalletId == currentWalletId
    return recipientWalletId in ownedWalletIds || senderWalletId !in ownedWalletIds
}

fun Transaction.getLabel(currentWalletId: Long?, ownedWalletIds: Set<Long> = emptySet()): String {
    if (!label.isNullOrBlank()) return label
    return if (direction == "INTERNAL") "Wallet Transfer" else if (isIncoming(currentWalletId, ownedWalletIds)) "Income" else "Outcome"
}

fun Transaction.getUiColor(currentWalletId: Long?, ownedWalletIds: Set<Long> = emptySet()): Color {
    if (currentWalletId == -1L) return Green // For card-only views where every transaction is a Top-up
    if (currentWalletId == null && direction == "INTERNAL") return Orange
    return if (isIncoming(currentWalletId, ownedWalletIds)) Green else Red
}

fun Transaction.getUiSign(currentWalletId: Long?, ownedWalletIds: Set<Long> = emptySet()): String {
    if (currentWalletId == -1L) return "+" // For card-only views
    if (currentWalletId == null && direction == "INTERNAL") return ""
    return if (isIncoming(currentWalletId, ownedWalletIds)) "+" else "-"
}

fun Transaction.getUiIcon(currentWalletId: Long?, ownedWalletIds: Set<Long> = emptySet()): ImageVector {
    if (currentWalletId == -1L) return Icons.Default.ArrowUpward
    if (direction == "INTERNAL") return Icons.Default.SwapHoriz
    return if (isIncoming(currentWalletId, ownedWalletIds)) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward
}

fun Transaction.getSymbol(isIncoming: Boolean, currencies: Map<String, com.krushkov.virtualwallet.domain.models.outputs.currency.Currency>): String {
    val currencyModel = if (isIncoming) recipientCurrency else senderCurrency
    if (currencyModel?.symbol != null) return currencyModel.symbol
    
    val code = if (isIncoming) recipientCurrencyCode else senderCurrencyCode
    return currencies[code]?.symbol ?: code ?: ""
}

fun Transaction.getFormattedDate(): String {
    return createdAt?.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")) ?: "Unknown date"
}
