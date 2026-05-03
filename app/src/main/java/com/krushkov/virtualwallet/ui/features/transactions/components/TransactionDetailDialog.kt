package com.krushkov.virtualwallet.ui.features.transactions.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krushkov.virtualwallet.R
import com.krushkov.virtualwallet.domain.models.outputs.currency.Currency
import com.krushkov.virtualwallet.domain.models.outputs.transaction.TransactionType
import com.krushkov.virtualwallet.domain.models.outputs.transaction.Transaction
import com.krushkov.virtualwallet.ui.core.CircleButton
import com.krushkov.virtualwallet.ui.core.Dialog
import com.krushkov.virtualwallet.ui.theme.*
import com.krushkov.virtualwallet.ui.utils.*

@Composable
fun TransactionDetailDialog(
    transaction: Transaction,
    currentWalletId: Long?,
    ownedWalletIds: Set<Long> = emptySet(),
    currencies: Map<String, Currency> = emptyMap(),
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CircleButton(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = CloudWhite,
                        modifier = Modifier.size(20.dp)
                    )
                },
                onClick = onDismiss,
                modifier = Modifier.align(Alignment.Start),
                containerColor = CyanNeon.copy(alpha = 0.5f)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = stringResource(R.string.title_transaction_details),
                color = CloudWhite,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(16.dp))

            val isIncoming = transaction.isIncoming(currentWalletId, ownedWalletIds)
            val color = transaction.getUiColor(currentWalletId, ownedWalletIds)
            val sign = transaction.getUiSign(currentWalletId, ownedWalletIds)
            val amount = if (isIncoming) transaction.recipientAmount else transaction.senderAmount
            
            val currencySymbol = transaction.getSymbol(isIncoming, currencies)
            
            Text(
                text = "$sign $amount $currencySymbol",
                color = color,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            HorizontalDivider(color = CloudWhite.copy(alpha = 0.1f))

            Spacer(modifier = Modifier.height(16.dp))

            val typeLabel = when (transaction.type) {
                TransactionType.TRANSFER -> stringResource(R.string.label_type_transfer)
                TransactionType.TOP_UP   -> stringResource(R.string.label_type_topup)
                TransactionType.PAYMENT  -> stringResource(R.string.label_type_payment)
                TransactionType.UNKNOWN  -> stringResource(R.string.label_unknown)
            }

            DetailRow(label = stringResource(R.string.detail_label), value = transaction.getLabel(currentWalletId, ownedWalletIds))
            DetailRow(label = stringResource(R.string.detail_date), value = transaction.getFormattedDate())
            DetailRow(label = stringResource(R.string.detail_type), value = typeLabel)

            transaction.label?.let {
                DetailRow(label = stringResource(R.string.detail_note), value = it)
            }

            if (isIncoming) {
                transaction.sender?.let {
                    DetailRow(
                        label = stringResource(R.string.label_from),
                        value = "${it.firstName ?: ""} ${it.lastName ?: ""}".trim().ifEmpty { stringResource(R.string.label_system) }
                    )
                }
            } else {
                transaction.recipient?.let {
                    DetailRow(label = stringResource(R.string.label_to), value = "${it.firstName ?: ""} ${it.lastName ?: ""}".trim().ifEmpty { it.username })
                }
            }

            transaction.externalReference?.let {
                DetailRow(label = stringResource(R.string.detail_reference), value = it)
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = CloudWhite.copy(alpha = 0.6f),
            fontSize = 14.sp
        )
        Text(
            text = value,
            color = CloudWhite,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
