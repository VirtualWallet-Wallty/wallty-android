package com.krushkov.virtualwallet.ui.features.transactions

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.krushkov.virtualwallet.domain.models.outputs.transaction.Transaction
import com.krushkov.virtualwallet.ui.core.*
import com.krushkov.virtualwallet.ui.features.transactions.components.TransactionDetailDialog
import com.krushkov.virtualwallet.ui.features.transactions.components.TransactionItem
import com.krushkov.virtualwallet.ui.theme.CyanNeon
import com.krushkov.virtualwallet.viewmodel.TransactionsViewModel

@Composable
fun TransactionsScreen(
    navController: NavController,
    viewModel: TransactionsViewModel = hiltViewModel()
) {
    val state = viewModel.state
    var selectedTransaction by remember { mutableStateOf<Transaction?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topContent = {},
            cardTitle = "All Transactions",
            cardContent = {
                state.transactions.forEachIndexed { index, transaction ->
                    if (index >= state.transactions.size - 1 && !state.isEndReached && !state.isMoreLoading) {
                        viewModel.loadNextPage()
                    }

                    state.currentWalletId?.let { walletId ->
                        TransactionItem(
                            transaction = transaction,
                            currentWalletId = walletId,
                            onTransactionClick = { selectedTransaction = it }
                        )
                    }
                }

                if (state.isMoreLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = CyanNeon)
                    }
                }
            }
        )

        selectedTransaction?.let { transaction ->
            state.currentWalletId?.let { walletId ->
                TransactionDetailDialog(
                    transaction = transaction,
                    currentWalletId = walletId,
                    onDismiss = { selectedTransaction = null }
                )
            }
        }
    }
}
