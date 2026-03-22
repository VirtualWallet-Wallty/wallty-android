package com.krushkov.virtualwallet.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.krushkov.virtualwallet.viewmodel.AuthViewModel
import com.krushkov.virtualwallet.viewmodel.AuthState

@Composable
fun LoginScreen(viewModel: AuthViewModel, navController: NavController) {

    val state by viewModel.authState.observeAsState(AuthState.Loading)

    var identifier by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column {

        TextField(
            value = identifier,
            onValueChange = { identifier = it },
            label = { Text("Username or Email") }
        )

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") }
        )

        Button(onClick = {
            viewModel.login(identifier, password)
        }) {
            Text("Login")
        }

        when (val currentState = state) {

            is AuthState.Loading -> {
                Text("Loading...")
            }

            is AuthState.Error -> {
                Text(currentState.message)
            }

            is AuthState.Authenticated -> {
                Text("Welcome ${currentState.user.username}")
            }

            else -> {}
        }
    }
}