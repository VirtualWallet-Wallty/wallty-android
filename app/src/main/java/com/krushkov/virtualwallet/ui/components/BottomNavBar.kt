package com.krushkov.virtualwallet.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BottomNavBar() {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF123C44))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {

        Text("Home", color = Color.White)
        Text("Wallets", color = Color.Gray)
        Text("Cards", color = Color.Gray)
        Text("Profile", color = Color.Gray)
    }
}