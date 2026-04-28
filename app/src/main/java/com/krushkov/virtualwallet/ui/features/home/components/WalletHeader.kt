package com.krushkov.virtualwallet.ui.features.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.krushkov.virtualwallet.domain.models.outputs.wallet.Wallet
import com.krushkov.virtualwallet.ui.theme.CloudWhite

@Composable
fun WalletHeader(wallet: Wallet) {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Text(
            text = wallet.name,
            color = CloudWhite
        )

        Text(
            text = "${wallet.balance} ${wallet.currency?.symbol ?: wallet.currencyCode}",
            color = CloudWhite,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
