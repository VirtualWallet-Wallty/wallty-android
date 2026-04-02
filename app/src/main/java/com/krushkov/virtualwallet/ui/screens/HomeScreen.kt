package com.krushkov.virtualwallet.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.krushkov.virtualwallet.ui.components.BottomNavBar
import com.krushkov.virtualwallet.ui.components.HeroWalletSection
import com.krushkov.virtualwallet.ui.components.TransactionItem
import com.krushkov.virtualwallet.ui.components.TransactionsSection
import com.krushkov.virtualwallet.ui.components.WalletHeader
import com.krushkov.virtualwallet.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {

    val state = viewModel.state

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A2A33))
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            state.wallet?.let { wallet ->

                HeroWalletSection(
                    wallet = wallet,
                    onAllWalletsClick = {
                        // TODO: navigate later
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))

                TransactionsSection(
                    transactions = state.transactions,
                    currentWalletId = wallet.id
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            BottomNavBar()
        }

        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }

        state.error?.let {
            Text(
                text = it,
                color = Color.Red,
                fontSize = 16.sp,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}