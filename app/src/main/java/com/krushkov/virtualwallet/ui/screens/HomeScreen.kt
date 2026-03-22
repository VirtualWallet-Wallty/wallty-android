package com.krushkov.virtualwallet.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.krushkov.virtualwallet.viewmodel.AuthViewModel

@Composable
fun HomeScreen(
    viewModel: AuthViewModel
) {
    Column {

        Text("Home Screen 🚀")

        Button(onClick = {
            viewModel.logout()
        }) {
            Text("Logout")
        }
    }
}