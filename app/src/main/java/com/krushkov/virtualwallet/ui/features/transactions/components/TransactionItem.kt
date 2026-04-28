package com.krushkov.virtualwallet.ui.features.transactions.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krushkov.virtualwallet.domain.models.outputs.currency.Currency
import com.krushkov.virtualwallet.domain.models.outputs.transaction.Transaction
import com.krushkov.virtualwallet.ui.theme.*
import com.krushkov.virtualwallet.ui.utils.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TransactionItem(
    transaction: Transaction,
    currentWalletId: Long,
    currencies: Map<String, Currency> = emptyMap(),
    onTransactionClick: (Transaction) -> Unit = {}
) {
    val isIncoming = transaction.isIncoming(currentWalletId)
    val color = transaction.getUiColor(currentWalletId)
    val sign = transaction.getUiSign(currentWalletId)
    val icon = transaction.getUiIcon(currentWalletId)
    
    val amount = if (isIncoming) transaction.recipientAmount else transaction.senderAmount
    val currencyCode = if (isIncoming) transaction.recipientCurrencyCode else transaction.senderCurrencyCode
    val currencySymbol = currencies[currencyCode]?.symbol ?: currencyCode ?: ""

    Surface(
        onClick = { onTransactionClick(transaction) },
        color = Color.Transparent,
        shape = AppCardShape
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon Container
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Info Column
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.getLabel(currentWalletId),
                    color = CloudWhite,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.2.sp
                    ),
                    maxLines = 1,
                    modifier = Modifier.basicMarquee()
                )
                
                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = transaction.getFormattedDate(),
                    color = CloudWhite.copy(alpha = 0.5f),
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Amount Column
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "$sign$amount $currencySymbol",
                    color = color,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Clip
                )
            }
        }
    }

}

@Composable
fun TransactionItemShimmer() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .shimmerEffect()
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Box(
                modifier = Modifier
                    .height(18.dp)
                    .width(140.dp)
                    .clip(AppCardShape)
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.height(6.dp))
            Box(
                modifier = Modifier
                    .height(14.dp)
                    .width(80.dp)
                    .clip(AppCardShape)
                    .shimmerEffect()
            )
        }

        Box(
            modifier = Modifier
                .height(20.dp)
                .width(70.dp)
                .clip(AppCardShape)
                .shimmerEffect()
        )
    }
}
