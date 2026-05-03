package com.krushkov.virtualwallet.ui.features.transactions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.krushkov.virtualwallet.R
import com.krushkov.virtualwallet.domain.models.outputs.transaction.Transaction
import com.krushkov.virtualwallet.domain.models.outputs.transaction.TransactionType
import com.krushkov.virtualwallet.domain.models.outputs.user.UserPreview
import com.krushkov.virtualwallet.ui.core.CircleButton
import com.krushkov.virtualwallet.ui.core.LoadingOverlay
import com.krushkov.virtualwallet.ui.theme.CloudWhite
import com.krushkov.virtualwallet.ui.theme.CyanNeon
import com.krushkov.virtualwallet.ui.theme.Red
import com.krushkov.virtualwallet.ui.utils.getFormattedDate
import com.krushkov.virtualwallet.viewmodel.TransactionDetailsViewModel

@Composable
fun TransactionDetailsScreen(
    navController: NavController,
    viewModel: TransactionDetailsViewModel = hiltViewModel()
) {
    val state = viewModel.state

    if (state.isLoading) {
        LoadingOverlay()
        return
    }

    val transaction = state.transaction

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            CircleButton(
                icon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.action_back),
                        tint = CloudWhite,
                        modifier = Modifier.size(20.dp)
                    )
                },
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.CenterStart),
                containerColor = CyanNeon.copy(alpha = 0.5f)
            )

            Text(
                text = stringResource(R.string.title_transaction_details),
                color = CloudWhite,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (transaction == null) {
            Text(
                text = stringResource(R.string.msg_no_transactions_found),
                color = CloudWhite.copy(alpha = 0.5f),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 48.dp)
            )
            return
        }

        TransactionDetailsContent(transaction)
    }
}

@Composable
private fun TransactionDetailsContent(transaction: Transaction) {
    val amount = transaction.recipientAmount
    val currency = transaction.recipientCurrency?.symbol
        ?: transaction.recipientCurrency?.code
        ?: transaction.recipientCurrencyCode
        ?: transaction.senderCurrency?.symbol
        ?: transaction.senderCurrency?.code
        ?: transaction.senderCurrencyCode
        ?: stringResource(R.string.label_unknown)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = transaction.label?.takeIf { it.isNotBlank() } ?: stringResource(R.string.label_unknown),
            color = CloudWhite,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "$amount $currency",
            color = Red,
            fontSize = 38.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(48.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DetailRow(
                label = stringResource(R.string.detail_type),
                value = transaction.type.toDisplayLabel()
            )
            DetailRow(
                label = stringResource(R.string.label_amount),
                value = amount.toString()
            )
            DetailRow(
                label = stringResource(R.string.label_currency),
                value = currency
            )
            transaction.sender?.fullName()?.takeIf { it.isNotBlank() }?.let {
                DetailRow(label = stringResource(R.string.label_from), value = it)
            }
            transaction.recipient?.fullName()?.takeIf { it.isNotBlank() }?.let {
                DetailRow(label = stringResource(R.string.label_to), value = it)
            }
            DetailRow(
                label = stringResource(R.string.detail_date),
                value = transaction.getFormattedDate()
            )
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = label,
            color = CloudWhite.copy(alpha = 0.45f),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            color = CloudWhite,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun TransactionType.toDisplayLabel(): String {
    return when (this) {
        TransactionType.TRANSFER -> stringResource(R.string.label_type_transfer)
        TransactionType.TOP_UP -> stringResource(R.string.label_type_topup)
        TransactionType.PAYMENT -> stringResource(R.string.label_type_payment)
        TransactionType.UNKNOWN -> stringResource(R.string.label_unknown)
    }
}

private fun UserPreview.fullName(): String {
    return "${firstName ?: ""} ${lastName ?: ""}".trim().ifEmpty { username }
}
