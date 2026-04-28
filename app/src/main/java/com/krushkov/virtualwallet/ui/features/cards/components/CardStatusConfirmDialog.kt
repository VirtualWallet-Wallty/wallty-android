package com.krushkov.virtualwallet.ui.features.cards.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krushkov.virtualwallet.ui.core.Button
import com.krushkov.virtualwallet.ui.core.CircleButton
import com.krushkov.virtualwallet.ui.core.Dialog
import com.krushkov.virtualwallet.ui.theme.CloudWhite
import com.krushkov.virtualwallet.ui.theme.CyanNeon
import com.krushkov.virtualwallet.ui.theme.Green
import com.krushkov.virtualwallet.ui.theme.Red

@Composable
fun CardStatusConfirmDialog(
    isDeactivating: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = if (isDeactivating) "Do you confirm deactivation for this card?"
                       else "Do you confirm activation for this card?",
                color = CloudWhite,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircleButton(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = CloudWhite,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    onClick = onDismiss,
                    containerColor = CyanNeon.copy(alpha = 0.2f)
                )

                Button(
                    text = "Accept",
                    onClick = onConfirm,
                    containerColor = if (isDeactivating) Red.copy(alpha = 0.5f) else Green.copy(alpha = 0.5f)
                )
            }
        }
    }
}
