package com.krushkov.virtualwallet.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.krushkov.virtualwallet.domain.models.outputs.transaction.Transaction

@Composable
fun TransactionsSection(
    transactions: List<Transaction>,
    currentWalletId: Long
) {

    Column {

        Text(
            text = "Latest transactions",
            color = Color.White,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(12.dp))

        transactions.forEach { transaction ->
            TransactionItem(
                transaction = transaction,
                currentWalletId = currentWalletId
            )
        }
    }
}