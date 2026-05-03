package com.krushkov.virtualwallet.ui.features.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.krushkov.virtualwallet.R
import com.krushkov.virtualwallet.ui.core.Button
import com.krushkov.virtualwallet.ui.core.CircleButton
import com.krushkov.virtualwallet.ui.core.DropdownField
import com.krushkov.virtualwallet.ui.core.IconTextButton
import com.krushkov.virtualwallet.ui.core.Scaffold
import com.krushkov.virtualwallet.ui.core.TextField
import com.krushkov.virtualwallet.ui.theme.CloudWhite
import com.krushkov.virtualwallet.ui.theme.CyanNeon
import com.krushkov.virtualwallet.ui.theme.Yellow
import com.krushkov.virtualwallet.viewmodel.CreateWalletViewModel

@Composable
fun CreateWalletScreen(
    navController: NavController,
    viewModel: CreateWalletViewModel = hiltViewModel()
) {
    val state = viewModel.state

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            viewModel.resetSuccess()
            navController.popBackStack()
        }
    }

    Scaffold(
        topContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                CircleButton(
                    icon = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = CloudWhite
                        )
                    },
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.align(Alignment.CenterStart)
                )
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.title_new_wallet),
                        color = CloudWhite,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.subtitle_new_wallet),
                        color = CloudWhite.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )
                }
            }
        },
        cardTitle = stringResource(R.string.label_wallet_details),
        cardContent = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                TextField(
                    value = state.name,
                    onValueChange = { viewModel.onNameChange(it) },
                    label = stringResource(R.string.label_wallet_name)
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = stringResource(R.string.label_currency),
                        color = CloudWhite,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(start = 4.dp)
                    )

                    DropdownField(
                        value = state.selectedCurrency?.let { "${it.code} - ${it.name}" } ?: "",
                        placeholder = stringResource(R.string.label_select_currency),
                        expanded = state.isCurrencyMenuExpanded,
                        onExpandedChange = { viewModel.toggleCurrencyMenu(it) },
                        items = {
                            state.currencies.forEach { currency ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = "${currency.code} - ${currency.name}",
                                            color = CloudWhite
                                        )
                                    },
                                    onClick = {
                                        viewModel.onCurrencySelect(currency)
                                        viewModel.toggleCurrencyMenu(false)
                                    }
                                )
                            }
                        }
                    )
                }

                if (state.hasWallets) {
                    IconTextButton(
                        icon = {
                            Icon(
                                imageVector = if (state.makeDefault) Icons.Default.Star else Icons.Outlined.StarBorder,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = CloudWhite
                            )
                        },
                        text = if (state.makeDefault) {
                            stringResource(R.string.action_default)
                        } else {
                            stringResource(R.string.label_set_as_default)
                        },
                        onClick = { viewModel.onMakeDefaultChange(!state.makeDefault) },
                        modifier = Modifier.fillMaxWidth(),
                        iconSize = 20.dp,
                        containerColor = if (state.makeDefault) Yellow.copy(alpha = 0.72f) else CyanNeon.copy(alpha = 0.2f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    text = stringResource(R.string.action_create),
                    onClick = { viewModel.createWallet() },
                    isLoading = state.isLoading,
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = CyanNeon.copy(alpha = 0.5f)
                )
            }
        }
    )
}
