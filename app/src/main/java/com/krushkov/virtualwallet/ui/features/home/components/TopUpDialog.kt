package com.krushkov.virtualwallet.ui.features.home.components

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
import com.krushkov.virtualwallet.domain.models.outputs.card.Card
import com.krushkov.virtualwallet.ui.core.Button
import com.krushkov.virtualwallet.ui.core.Dialog
import com.krushkov.virtualwallet.ui.theme.*
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopUpDialog(
    cards: List<Card>,
    currencyCode: String,
    onDismiss: () -> Unit,
    onConfirm: (cardId: Long, amount: BigDecimal) -> Unit
) {
    var amount by remember { mutableStateOf("") }
    var selectedCardId by remember { mutableStateOf(cards.firstOrNull()?.id ?: -1L) }
    var expanded by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Top-up Wallet",
                color = CloudWhite,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = amount,
                onValueChange = { if (it.all { char -> char.isDigit() || char == '.' }) amount = it },
                label = { Text("Amount ($currencyCode)", color = CloudWhite.copy(alpha = 0.6f)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = CloudWhite,
                    unfocusedTextColor = CloudWhite,
                    focusedBorderColor = CyanNeon,
                    unfocusedBorderColor = CloudWhite.copy(alpha = 0.3f)
                )
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                val selectedCard = cards.find { it.id == selectedCardId }
                OutlinedTextField(
                    value = "••${selectedCard?.cardSuffix ?: "Select Card"}",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Select Card", color = CloudWhite.copy(alpha = 0.6f)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
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
                    cards.forEach { card ->
                        DropdownMenuItem(
                            text = { Text("Card ••${card.cardSuffix}", color = CloudWhite) },
                            onClick = {
                                selectedCardId = card.id
                                expanded = false
                            }
                        )
                    }
                }
            }

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
                        val amountDecimal = amount.toBigDecimalOrNull()
                        if (amountDecimal != null && selectedCardId != -1L) {
                            onConfirm(selectedCardId, amountDecimal)
                        }
                    },
                    containerColor = Green.copy(alpha = 0.3f)
                )
            }
        }
    }
}
