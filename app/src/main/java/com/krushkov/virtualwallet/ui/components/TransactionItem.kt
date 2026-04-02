package com.krushkov.virtualwallet.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krushkov.virtualwallet.domain.models.outputs.transaction.Transaction
import com.krushkov.virtualwallet.ui.utils.getFormattedDate
import com.krushkov.virtualwallet.ui.utils.getTitle
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward

@Composable
fun TransactionItem(
    transaction: Transaction,
    currentWalletId: Long
) {

    val isIncoming = transaction.recipientWalletId == currentWalletId

    val amountColor = if (isIncoming) Color(0xFF42B237) else Color(0xFFCC2A2A)
    val sign = if (isIncoming) "+" else "-"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        // LEFT PART
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = if (isIncoming)
                    Icons.Default.ArrowDownward
                else
                    Icons.Default.ArrowUpward,
                contentDescription = null,
                tint = amountColor,
                modifier = Modifier.size(28.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {

                Text(
                    text = transaction.getTitle(currentWalletId),
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = transaction.getFormattedDate(),
                    color = Color.LightGray,
                    fontSize = 12.sp
                )
            }
        }

        // RIGHT PART
        Text(
            text = "$sign ${transaction.amount} €",
            color = amountColor,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
    }
}