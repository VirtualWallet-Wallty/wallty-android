package com.krushkov.virtualwallet.ui.features.transfer

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.krushkov.virtualwallet.ui.core.Button
import com.krushkov.virtualwallet.ui.core.CircleButton
import com.krushkov.virtualwallet.ui.core.DropdownField
import com.krushkov.virtualwallet.ui.core.GlassSurface
import com.krushkov.virtualwallet.ui.nav.Routes
import com.krushkov.virtualwallet.ui.theme.AppCardShape
import com.krushkov.virtualwallet.ui.theme.CloudWhite
import com.krushkov.virtualwallet.ui.theme.CyanNeon
import com.krushkov.virtualwallet.ui.theme.Green
import com.krushkov.virtualwallet.ui.theme.Red
import com.krushkov.virtualwallet.viewmodel.TransferViewModel

@Composable
fun SendConfirmScreen(
    navController: NavController,
    viewModel: TransferViewModel
) {
    val state = viewModel.state
    val recipient = state.recipientProfile
    val symbol = state.currencySymbol

    BackHandler {
        viewModel.resetScanState()
        navController.popBackStack()
    }

    LaunchedEffect(state.isTransferSuccess) {
        if (state.isTransferSuccess) {
            viewModel.resetTransferSuccess()
            navController.navigate(Routes.HOME) {
                popUpTo(Routes.HOME) { inclusive = false }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            CircleButton(
                icon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = CloudWhite
                    )
                },
                onClick = {
                    viewModel.resetScanState()
                    navController.popBackStack()
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Send Transfer",
            color = CloudWhite,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Confirm the details below",
            color = CloudWhite.copy(alpha = 0.6f),
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Amount input — transparent BasicTextField drives input; display row is always centered
        val isEmpty = state.sendAmount.isEmpty()
        BasicTextField(
            value = state.sendAmount,
            onValueChange = { if (it.all { c -> c.isDigit() || c == '.' }) viewModel.onSendAmountChange(it) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            textStyle = TextStyle(color = Color.Transparent, fontSize = 1.sp),
            singleLine = true,
            decorationBox = { innerTextField ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = if (isEmpty) "0" else state.sendAmount,
                            color = if (isEmpty) CloudWhite.copy(alpha = 0.25f) else CloudWhite,
                            fontSize = 52.sp,
                            fontWeight = FontWeight.Bold
                        )
                        if (symbol.isNotBlank()) {
                            Text(
                                text = " $symbol",
                                color = CloudWhite.copy(alpha = if (isEmpty) 0.25f else 1f),
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Normal,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                    }
                    // Invisible inner field keeps focus and keyboard
                    Box(modifier = Modifier
                        .matchParentSize()
                        .alpha(0f)) {
                        innerTextField()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(48.dp))

        // From wallet
        Text(
            text = "From",
            color = CloudWhite,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, bottom = 8.dp)
        )

        DropdownField(
            value = state.selectedWallet?.let {
                "${it.name} (${it.currencyCode ?: it.currency?.code ?: ""})"
            } ?: "",
            placeholder = "Select wallet",
            expanded = state.isWalletDropdownExpanded,
            onExpandedChange = { viewModel.toggleWalletDropdown(it) },
            items = {
                state.wallets.forEach { wallet ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                "${wallet.name} (${wallet.currencyCode ?: wallet.currency?.code ?: ""})",
                                color = CloudWhite
                            )
                        },
                        onClick = { viewModel.selectWallet(wallet) }
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Recipient
        Text(
            text = "To",
            color = CloudWhite,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, bottom = 12.dp)
        )

        GlassSurface(
            modifier = Modifier.fillMaxWidth(),
            shape = AppCardShape
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    GlassSurface(
                        modifier = Modifier.fillMaxSize(),
                        shape = CircleShape,
                        containerColor = CyanNeon.copy(alpha = 0.15f)
                    ) {}
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = null,
                        tint = CyanNeon,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    val displayName = when {
                        recipient != null ->
                            "${recipient.firstName} ${recipient.lastName}".trim()
                        else -> "Unknown"
                    }
                    Text(
                        text = displayName,
                        color = CloudWhite,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (recipient != null) {
                        Text(
                            text = "@${recipient.username}",
                            color = CloudWhite.copy(alpha = 0.55f),
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                text = "Cancel",
                onClick = {
                    viewModel.resetScanState()
                    navController.popBackStack()
                },
                containerColor = Red.copy(alpha = 0.3f),
                modifier = Modifier.weight(1f)
            )
            Button(
                text = "Confirm",
                onClick = { viewModel.sendTransfer() },
                isLoading = state.isSendLoading,
                enabled = state.sendAmount.toBigDecimalOrNull() != null
                        && state.selectedWallet != null,
                containerColor = Green.copy(alpha = 0.4f),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
