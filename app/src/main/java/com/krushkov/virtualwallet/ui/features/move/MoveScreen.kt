package com.krushkov.virtualwallet.ui.features.move

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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.graphics.vector.rememberVectorPainter
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
import com.krushkov.virtualwallet.ui.theme.Black
import com.krushkov.virtualwallet.ui.theme.CloudWhite
import com.krushkov.virtualwallet.ui.theme.CyanNeon
import com.krushkov.virtualwallet.ui.theme.Green
import com.krushkov.virtualwallet.viewmodel.MoveViewModel

@Composable
fun MoveScreen(
    navController: NavController,
    viewModel: MoveViewModel = hiltViewModel()
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

    val switchIcon = remember {
        ImageVector.Builder(
            name = "Switch",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 16f,
            viewportHeight = 16f
        ).path(
            stroke = SolidColor(Color.White),
            strokeLineWidth = 1f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round
        ) {
            moveTo(5f, 11f); lineTo(5f, 4f)
            moveTo(2.5f, 6.5f); lineTo(5f, 4f); lineTo(7.5f, 6.5f)
            moveTo(11f, 5f); lineTo(11f, 12f)
            moveTo(8.5f, 9.5f); lineTo(11f, 12f); lineTo(13.5f, 9.5f)
        }.build()
    }

    val fromCode = state.fromWallet?.let { it.currencyCode ?: it.currency?.code }
    val toCode = state.selectedToWallet?.let { it.currencyCode ?: it.currency?.code }
    val showCurrencyToggle = fromCode != null && toCode != null && fromCode != toCode

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
                text = stringResource(R.string.title_move),
                color = CloudWhite,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.subtitle_move),
                color = CloudWhite.copy(alpha = 0.6f),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

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

            Text(
                text = stringResource(R.string.label_from),
                color = CloudWhite,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, bottom = 8.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                DropdownField(
                    modifier = Modifier.weight(1f),
                    value = state.fromWallet?.name ?: "",
                    placeholder = stringResource(R.string.label_select_wallet),
                    expanded = state.isFromDropdownExpanded,
                    onExpandedChange = { viewModel.toggleFromDropdown(it) },
                    items = {
                        state.wallets.forEach { wallet ->
                            DropdownMenuItem(
                                text = { Text(wallet.name, color = CloudWhite) },
                                onClick = { viewModel.selectFromWallet(wallet) }
                            )
                        }
                    }
                )
                if (fromCode != null) {
                    CircleButton(
                        icon = {
                            Text(
                                text = fromCode,
                                color = CloudWhite,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        },
                        onClick = { if (showCurrencyToggle) viewModel.selectCurrency(fromCode) },
                        containerColor = if (!showCurrencyToggle || state.selectedCurrencyCode == fromCode)
                            CyanNeon.copy(alpha = 0.4f) else Black.copy(alpha = 0.4f),
                        enabled = showCurrencyToggle,
                        modifier = Modifier.size(56.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircleButton(
                    icon = {
                        Icon(
                            painter = rememberVectorPainter(image = switchIcon),
                            contentDescription = null,
                            tint = Color.Unspecified
                        )
                    },
                    onClick = { viewModel.switchWallets() },
                    containerColor = CyanNeon.copy(alpha = 0.2f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.label_to),
                color = CloudWhite,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, bottom = 8.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                DropdownField(
                    modifier = Modifier.weight(1f),
                    value = state.selectedToWallet?.name ?: "",
                    placeholder = stringResource(R.string.label_select_wallet),
                    expanded = state.isDropdownExpanded,
                    onExpandedChange = { viewModel.toggleDropdown(it) },
                    items = {
                        state.wallets.forEach { wallet ->
                            DropdownMenuItem(
                                text = { Text(wallet.name, color = CloudWhite) },
                                onClick = { viewModel.selectToWallet(wallet) }
                            )
                        }
                    }
                )
                if (toCode != null) {
                    CircleButton(
                        icon = {
                            Text(
                                text = toCode,
                                color = CloudWhite,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        },
                        onClick = { if (showCurrencyToggle) viewModel.selectCurrency(toCode) },
                        containerColor = if (!showCurrencyToggle || state.selectedCurrencyCode == toCode)
                            CyanNeon.copy(alpha = 0.4f) else Black.copy(alpha = 0.4f),
                        enabled = showCurrencyToggle,
                        modifier = Modifier.size(56.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }

        Button(
            text = stringResource(R.string.action_confirm),
            onClick = { viewModel.confirm() },
            isLoading = state.isSubmitLoading,
            enabled = state.amount.toBigDecimalOrNull() != null && state.selectedToWallet != null,
            containerColor = Green.copy(alpha = 0.4f),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        )
    }
}
