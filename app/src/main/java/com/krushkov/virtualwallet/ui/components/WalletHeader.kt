package com.krushkov.virtualwallet.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.krushkov.virtualwallet.domain.models.outputs.wallet.Wallet

@Composable
fun WalletHeader(wallet: Wallet) {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Text(
            text = wallet.name,
            color = Color.Gray
        )

        Text(
            text = "${wallet.balance} ${wallet.currency?.symbol ?: wallet.currencyCode}",
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
    }
}