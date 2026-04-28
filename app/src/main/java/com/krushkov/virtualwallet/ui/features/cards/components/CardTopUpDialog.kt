package com.krushkov.virtualwallet.ui.features.cards.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krushkov.virtualwallet.domain.models.outputs.wallet.Wallet
import com.krushkov.virtualwallet.ui.core.Button
import com.krushkov.virtualwallet.ui.core.Dialog
import com.krushkov.virtualwallet.ui.theme.*
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardTopUpDialog(
    wallets: List<Wallet>,
    onDismiss: () -> Unit,
    onConfirm: (walletId: Long, currencyCode: String, amount: BigDecimal) -> Unit
) {
    var amount by remember { mutableStateOf("") }
    var selectedWallet by remember { mutableStateOf(wallets.firstOrNull()) }
    var expanded by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Top Up Card",
                color = CloudWhite,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedWallet?.name ?: "Select Wallet",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Select Wallet", color = CloudWhite.copy(alpha = 0.6f)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = CloudWhite,
                        unfocusedTextColor = CloudWhite,
                        focusedBorderColor = CyanNeon,
                        unfocusedBorderColor = CloudWhite.copy(alpha = 0.3f)
                    )
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(NightBlack)
                ) {
                    wallets.forEach { wallet ->
                        val currency = wallet.currency?.code ?: wallet.currencyCode ?: ""
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "${wallet.name} ($currency)",
                                    color = CloudWhite
                                )
                            },
                            onClick = {
                                selectedWallet = wallet
                                expanded = false
                            }
                        )
                    }
                }
            }

            val currencyCode = selectedWallet?.currency?.code ?: selectedWallet?.currencyCode ?: ""
            OutlinedTextField(
                value = amount,
                onValueChange = { if (it.all { char -> char.isDigit() || char == '.' }) amount = it },
                label = {
                    Text(
                        text = if (currencyCode.isNotEmpty()) "Amount ($currencyCode)" else "Amount",
                        color = CloudWhite.copy(alpha = 0.6f)
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = CloudWhite,
                    unfocusedTextColor = CloudWhite,
                    focusedBorderColor = CyanNeon,
                    unfocusedBorderColor = CloudWhite.copy(alpha = 0.3f)
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
            ) {
                Button(
                    text = "Cancel",
                    onClick = onDismiss,
                    containerColor = Red.copy(alpha = 0.2f)
                )

                Button(
                    text = "Confirm",
                    onClick = {
                        val wallet = selectedWallet
                        val amountDecimal = amount.toBigDecimalOrNull()
                        val code = wallet?.currency?.code ?: wallet?.currencyCode
                        if (wallet != null && amountDecimal != null && code != null) {
                            onConfirm(wallet.id, code, amountDecimal)
                        }
                    },
                    containerColor = Green.copy(alpha = 0.3f)
                )
            }
        }
    }
}
