package com.krushkov.virtualwallet.ui.features.transactions.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krushkov.virtualwallet.R
import com.krushkov.virtualwallet.ui.theme.CloudWhite
import com.krushkov.virtualwallet.ui.theme.CyanNeon
import com.krushkov.virtualwallet.viewmodel.states.TransactionsState

@Composable
fun TransactionsListContent(
    state: TransactionsState,
    ownedWalletIds: Set<Long>,
    onLoadNextPage: () -> Unit,
    onTransactionClick: (Long) -> Unit
) {
    state.transactions.forEachIndexed { index, transaction ->
        if (index >= state.transactions.size - 1 && !state.isEndReached && !state.isMoreLoading) {
            onLoadNextPage()
        }
        TransactionItem(
            transaction = transaction,
            currentWalletId = state.currentWalletId,
            ownedWalletIds = ownedWalletIds,
            currencies = state.currencies,
            onTransactionClick = { onTransactionClick(it.id) }
        )
    }

    if (state.transactions.isEmpty() && !state.isLoading) {
        Text(
            text = stringResource(R.string.msg_no_transactions_found),
            color = CloudWhite.copy(alpha = 0.5f),
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 32.dp)
        )
    }

    if (state.isMoreLoading) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = CyanNeon, modifier = Modifier.size(24.dp))
        }
    }
}
