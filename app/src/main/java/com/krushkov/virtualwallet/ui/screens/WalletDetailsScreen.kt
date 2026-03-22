package com.krushkov.virtualwallet.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.krushkov.virtualwallet.viewmodel.WalletViewModel

@Composable
fun WalletDetailsScreen(
    walletId: Long,
    viewModel: WalletViewModel
) {

    val wallet by viewModel.selectedWallet.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(false)

    LaunchedEffect(walletId) {
        viewModel.loadWalletDetails(walletId)
    }

    Column(modifier = Modifier.padding(16.dp)) {

        when {
            isLoading -> Text("Loading details...")

            wallet != null -> {
                Text("Wallet Details 💳")

                Spacer(modifier = Modifier.height(16.dp))

                Text("Name: ${wallet!!.name}")
                Text("Balance: ${wallet!!.balance}")
                Text("Currency: ${wallet!!.currency.code}")
                Text("Owner: ${wallet!!.owner.username}")
            }

            else -> Text("No data")
        }
    }
}