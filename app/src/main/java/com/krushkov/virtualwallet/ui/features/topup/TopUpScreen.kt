package com.krushkov.virtualwallet.ui.features.topup

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
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.AccountBalanceWallet
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.krushkov.virtualwallet.R
import com.krushkov.virtualwallet.ui.core.Button
import com.krushkov.virtualwallet.ui.core.CircleButton
import com.krushkov.virtualwallet.ui.core.DropdownField
import com.krushkov.virtualwallet.ui.core.GlassSurface
import com.krushkov.virtualwallet.ui.theme.AppCardShape
import com.krushkov.virtualwallet.ui.theme.CloudWhite
import com.krushkov.virtualwallet.ui.theme.CyanNeon
import com.krushkov.virtualwallet.ui.theme.Green
import com.krushkov.virtualwallet.viewmodel.TopUpViewModel

@Composable
fun TopUpScreen(
    navController: NavController,
    viewModel: TopUpViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val symbol = state.currencySymbol
    val isEmpty = state.amount.isEmpty()

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            viewModel.resetSuccess()
            navController.popBackStack()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
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
                    onClick = { navController.popBackStack() }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.title_top_up),
                color = CloudWhite,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(
                    if (state.isWalletMode) R.string.subtitle_top_up_wallet
                    else R.string.subtitle_top_up_card
                ),
                color = CloudWhite.copy(alpha = 0.6f),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(48.dp))

            BasicTextField(
                value = state.amount,
                onValueChange = { if (it.all { c -> c.isDigit() || c == '.' }) viewModel.onAmountChange(it) },
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
                                text = if (isEmpty) "0" else state.amount,
                                color = if (isEmpty) CloudWhite.copy(alpha = 0.25f) else CloudWhite,
                                fontSize = 52.sp,
                                fontWeight = FontWeight.Bold
                            )
                            if (symbol.isNotBlank()) {
                                Text(
                                    text = " $symbol",
                                    color = if (isEmpty) CloudWhite.copy(alpha = 0.25f) else CloudWhite,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Normal,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                            }
                        }
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .alpha(0f)
                        ) { innerTextField() }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(48.dp))

            if (state.isWalletMode) {
                Text(
                    text = stringResource(R.string.label_pay_with),
                    color = CloudWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, bottom = 8.dp)
                )
                DropdownField(
                    value = state.selectedCard?.let { "Card ••${it.cardSuffix}" } ?: "",
                    placeholder = stringResource(R.string.label_select_card),
                    expanded = state.isCardDropdownExpanded,
                    onExpandedChange = { viewModel.toggleCardDropdown(it) },
                    items = {
                        state.cards.forEach { card ->
                            DropdownMenuItem(
                                text = { Text("Card ••${card.cardSuffix}", color = CloudWhite) },
                                onClick = { viewModel.selectCard(card) }
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(28.dp))

                Text(
                    text = stringResource(R.string.label_wallet),
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
                                imageVector = Icons.Default.AccountBalanceWallet,
                                contentDescription = null,
                                tint = CyanNeon,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = state.wallet?.name ?: "",
                                color = CloudWhite,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            val balanceSymbol = state.wallet?.currency?.symbol ?: symbol
                            val balance = state.wallet?.balance
                            if (balance != null) {
                                Text(
                                    text = "$balance $balanceSymbol",
                                    color = CloudWhite.copy(alpha = 0.55f),
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            } else {
                Text(
                    text = stringResource(R.string.label_to_wallet),
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
                    placeholder = stringResource(R.string.label_select_wallet),
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

                Text(
                    text = stringResource(R.string.label_card),
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
                                imageVector = Icons.Default.CreditCard,
                                contentDescription = null,
                                tint = CyanNeon,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = state.card?.let { "Card ••${it.cardSuffix}" } ?: "",
                                color = CloudWhite,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            state.card?.cardHolder?.let {
                                Text(
                                    text = it,
                                    color = CloudWhite.copy(alpha = 0.55f),
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }

        Button(
            text = stringResource(R.string.action_confirm),
            onClick = { viewModel.confirm() },
            isLoading = state.isSubmitLoading,
            enabled = state.amount.toBigDecimalOrNull() != null &&
                    (if (state.isWalletMode) state.selectedCard != null else state.selectedWallet != null),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            containerColor = Green.copy(alpha = 0.4f)
        )
    }
}
