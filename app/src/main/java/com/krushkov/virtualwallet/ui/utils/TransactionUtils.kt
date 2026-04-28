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

fun Transaction.isIncoming(currentWalletId: Long): Boolean {
    return recipientWalletId == currentWalletId
}

fun Transaction.getLabel(currentWalletId: Long): String {
    if (!label.isNullOrBlank()) return label
    return if (direction == "INTERNAL") "Wallet Transfer" else if (isIncoming(currentWalletId)) "Income" else "Outcome"
}

fun Transaction.getUiColor(currentWalletId: Long): Color {
    if (currentWalletId == -1L) return Green // For card-only views where every transaction is a Top-up
    return if (isIncoming(currentWalletId)) Green else Red
}

fun Transaction.getUiSign(currentWalletId: Long): String {
    if (currentWalletId == -1L) return "+" // For card-only views
    return if (isIncoming(currentWalletId)) "+" else "-"
}

fun Transaction.getUiIcon(currentWalletId: Long): ImageVector {
    if (currentWalletId == -1L) return Icons.Default.ArrowUpward
    if (direction == "INTERNAL") return Icons.Default.SwapHoriz
    return if (isIncoming(currentWalletId)) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward
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
