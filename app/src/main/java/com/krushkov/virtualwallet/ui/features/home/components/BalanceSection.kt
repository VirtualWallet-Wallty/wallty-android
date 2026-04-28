package com.krushkov.virtualwallet.ui.features.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krushkov.virtualwallet.ui.core.Button
import com.krushkov.virtualwallet.ui.theme.CloudWhite

@Composable
fun BalanceSection(balance: String, onAllWalletsClick: () -> Unit = {}) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Personal", color = CloudWhite)
        Text(
            text = balance,
            color = CloudWhite,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            text = "All Wallets",
            onClick = onAllWalletsClick
        )
    }
}
