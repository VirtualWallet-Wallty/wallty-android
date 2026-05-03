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
import com.krushkov.virtualwallet.ui.core.Button
import com.krushkov.virtualwallet.ui.theme.CloudWhite
import com.krushkov.virtualwallet.ui.theme.CyanNeon

private val LatestTransactionItemHeight = 68.dp
private val LatestSeeAllReserveHeight = 52.dp

@Composable
fun TransactionsSection(
    title: String? = null,
    transactions: List<Transaction>,
    currentWalletId: Long?,
    ownedWalletIds: Set<Long> = emptySet(),
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
                    ownedWalletIds = ownedWalletIds,
                    currencies = currencies,
                    onTransactionClick = onTransactionClick
                )
            }
        }
    }
}

@Composable
fun LatestTransactionsSection(
    transactions: List<Transaction>,
    currentWalletId: Long?,
    ownedWalletIds: Set<Long> = emptySet(),
    currencies: Map<String, com.krushkov.virtualwallet.domain.models.outputs.currency.Currency> = emptyMap(),
    emptyMessage: String = "No transactions found.",
    seeAllText: String,
    onSeeAllClick: () -> Unit,
    onTransactionClick: (Transaction) -> Unit = {}
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
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
            return@BoxWithConstraints
        }

        val availableListHeight = (maxHeight - LatestSeeAllReserveHeight).coerceAtLeast(0.dp)
        val visibleCount = (availableListHeight.value / LatestTransactionItemHeight.value)
            .toInt()
            .coerceAtLeast(1)
            .coerceAtMost(transactions.size)

        Column(modifier = Modifier.fillMaxSize()) {
            Column {
                transactions.take(visibleCount).forEach { transaction ->
                    TransactionItem(
                        transaction = transaction,
                        currentWalletId = currentWalletId,
                        ownedWalletIds = ownedWalletIds,
                        currencies = currencies,
                        onTransactionClick = onTransactionClick
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                text = seeAllText,
                onClick = onSeeAllClick,
                modifier = Modifier.fillMaxWidth(),
                containerColor = CyanNeon.copy(alpha = 0.15f)
            )
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
