package com.krushkov.virtualwallet.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.krushkov.virtualwallet.viewmodel.WalletViewModel

@Composable
fun WalletListScreen(
    viewModel: WalletViewModel,
    navController: NavController
) {

    val wallets by viewModel.wallets.observeAsState(emptyList())
    val isLoading by viewModel.isLoading.observeAsState(false)
    val error by viewModel.error.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.loadWallets()
    }

    Column(modifier = Modifier.padding(16.dp)) {

        Text("My Wallets 💰", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        when {
            isLoading -> Text("Loading...")

            error != null -> Text("Error: $error")

            else -> {
                LazyColumn {
                    items(wallets) { wallet ->

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clickable {
                                    navController.navigate("wallet/${wallet.id}")
                                }
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(wallet.name)
                                Text("${wallet.balance} ${wallet.currencyCode}")
                            }
                        }
                    }
                }
            }
        }
    }
}