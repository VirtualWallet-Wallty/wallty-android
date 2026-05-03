package com.krushkov.virtualwallet.ui.features.payment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.krushkov.virtualwallet.ui.core.Button
import com.krushkov.virtualwallet.ui.core.LoadingOverlay
import com.krushkov.virtualwallet.ui.theme.CloudWhite
import com.krushkov.virtualwallet.ui.theme.Green
import com.krushkov.virtualwallet.ui.theme.Red
import com.krushkov.virtualwallet.viewmodel.PaymentConfirmViewModel

@Composable
fun PaymentConfirmScreen(
    navController: NavController,
    viewModel: PaymentConfirmViewModel = hiltViewModel()
) {
    val state = viewModel.state

    LaunchedEffect(state.isDone) {
        if (state.isDone) {
            navController.popBackStack()
        }
    }

    if (state.isLoading) {
        LoadingOverlay()
        return
    }

    val payment = state.pendingPayment ?: return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.title_payment_request),
            color = CloudWhite.copy(alpha = 0.7f),
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "${payment.amount} ${payment.currencyCode}",
            color = Red,
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = state.wallet?.name ?: stringResource(R.string.label_unknown),
            color = CloudWhite,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(36.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 420.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                text = stringResource(R.string.action_cancel),
                onClick = { viewModel.cancel() },
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 4.dp),
                containerColor = Red.copy(alpha = 0.4f)
            )
            Button(
                text = stringResource(R.string.action_pay),
                onClick = { viewModel.confirm() },
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 4.dp),
                containerColor = Green.copy(alpha = 0.5f)
            )
        }
    }
}
