package com.krushkov.virtualwallet.ui.features.transactions.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krushkov.virtualwallet.domain.models.outputs.transaction.Transaction
import com.krushkov.virtualwallet.ui.theme.CloudWhite
import com.krushkov.virtualwallet.ui.theme.CyanNeon

@Composable
fun TransactionsSection(
    title: String? = null,
    transactions: List<Transaction>,
    currentWalletId: Long,
    currencies: Map<String, com.krushkov.virtualwallet.domain.models.outputs.currency.Currency> = emptyMap(),
    emptyMessage: String = "No transactions found.",
    onTransactionClick: (Transaction) -> Unit = {}
) {
    Column {
        if (transactions.isEmpty()) {
            Text(
                text = emptyMessage,
                color = CloudWhite.copy(alpha = 0.5f),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp)
            )
        } else {
            transactions.forEach { transaction ->
                TransactionItem(
                    transaction = transaction,
                    currentWalletId = currentWalletId,
                    currencies = currencies,
                    onTransactionClick = onTransactionClick
                )
            }
        }
    }
}

@Composable
fun TransactionsSectionShimmer() {
    Column {
        repeat(5) {
            TransactionItemShimmer()
        }
    }
}
