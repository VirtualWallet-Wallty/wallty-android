package com.krushkov.virtualwallet.ui.features.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.krushkov.virtualwallet.ui.core.Button
import com.krushkov.virtualwallet.ui.core.DropdownField
import com.krushkov.virtualwallet.ui.core.Scaffold
import com.krushkov.virtualwallet.ui.core.TextField
import com.krushkov.virtualwallet.ui.theme.Black
import com.krushkov.virtualwallet.ui.theme.CloudWhite
import com.krushkov.virtualwallet.ui.theme.CyanNeon
import com.krushkov.virtualwallet.ui.theme.Red
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "New Wallet",
                    color = CloudWhite,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Personalize your wallet management",
                    color = CloudWhite.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
            }
        },
        cardTitle = "Wallet Details",
        cardContent = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                TextField(
                    value = state.name,
                    onValueChange = { viewModel.onNameChange(it) },
                    label = "Wallet Name"
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Currency",
                        color = CloudWhite,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(start = 4.dp)
                    )

                    DropdownField(
                        value = state.selectedCurrency?.let { "${it.code} - ${it.name}" } ?: "",
                        placeholder = "Select Currency",
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

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Set as Default",
                        color = CloudWhite,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Switch(
                        checked = state.makeDefault,
                        onCheckedChange = { viewModel.onMakeDefaultChange(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = CloudWhite,
                            checkedTrackColor = CyanNeon,
                            uncheckedThumbColor = CloudWhite.copy(alpha = 0.5f),
                            uncheckedTrackColor = Black.copy(alpha = 0.5f),
                            uncheckedBorderColor = CyanNeon.copy(alpha = 0.5f)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
                ) {
                    Button(
                        text = "Cancel",
                        onClick = { navController.popBackStack() },
                        containerColor = Red.copy(alpha = 0.3f)
                    )

                    Button(
                        text = "Create",
                        onClick = { viewModel.createWallet() },
                        isLoading = state.isLoading,
                        containerColor = CyanNeon.copy(alpha = 0.5f)
                    )
                }
            }
        }
    )
}
