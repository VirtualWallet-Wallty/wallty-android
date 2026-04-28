package com.krushkov.virtualwallet.ui.features.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
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
import com.krushkov.virtualwallet.ui.features.cards.components.CardNumberField
import com.krushkov.virtualwallet.ui.theme.CloudWhite
import com.krushkov.virtualwallet.ui.theme.CyanNeon
import com.krushkov.virtualwallet.ui.theme.Red
import com.krushkov.virtualwallet.viewmodel.AddCardViewModel
import java.util.Calendar

@Composable
fun AddCardScreen(
    navController: NavController,
    viewModel: AddCardViewModel = hiltViewModel()
) {
    val state = viewModel.state

    val months = remember { (1..12).map { it.toString().padStart(2, '0') } }
    val currentYear = remember { Calendar.getInstance().get(Calendar.YEAR) }
    val years = remember { (currentYear..currentYear + 10).map { it.toString() } }

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
                    text = "New Card",
                    color = CloudWhite,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Add your payment card",
                    color = CloudWhite.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
            }
        },
        cardTitle = "Card Details",
        cardContent = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                TextField(
                    value = state.cardHolder,
                    onValueChange = { viewModel.onCardHolderChange(it) },
                    label = "Card Holder"
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Card Number",
                        color = CloudWhite,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                    CardNumberField(
                        value = state.cardNumber,
                        onValueChange = { viewModel.onCardNumberChange(it) }
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Expiration",
                        color = CloudWhite,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        DropdownField(
                            value = state.expirationMonth,
                            placeholder = "Month",
                            expanded = state.isMonthMenuExpanded,
                            onExpandedChange = { viewModel.toggleMonthMenu(it) },
                            modifier = Modifier.weight(1f),
                            items = {
                                months.forEach { month ->
                                    DropdownMenuItem(
                                        text = { Text(month, color = CloudWhite) },
                                        onClick = { viewModel.onMonthSelect(month) }
                                    )
                                }
                            }
                        )

                        DropdownField(
                            value = state.expirationYear,
                            placeholder = "Year",
                            expanded = state.isYearMenuExpanded,
                            onExpandedChange = { viewModel.toggleYearMenu(it) },
                            modifier = Modifier.weight(1f),
                            items = {
                                years.forEach { year ->
                                    DropdownMenuItem(
                                        text = { Text(year, color = CloudWhite) },
                                        onClick = { viewModel.onYearSelect(year) }
                                    )
                                }
                            }
                        )
                    }
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
                        text = "Add",
                        onClick = { viewModel.addCard() },
                        isLoading = state.isLoading,
                        containerColor = CyanNeon.copy(alpha = 0.5f)
                    )
                }
            }
        }
    )
}
