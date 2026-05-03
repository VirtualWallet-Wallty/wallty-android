package com.krushkov.virtualwallet.ui.features.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.krushkov.virtualwallet.R
import com.krushkov.virtualwallet.ui.common.AppHeader
import com.krushkov.virtualwallet.ui.core.Button
import com.krushkov.virtualwallet.ui.core.TextField
import com.krushkov.virtualwallet.ui.theme.*
import com.krushkov.virtualwallet.viewmodel.AuthViewModel
import com.krushkov.virtualwallet.ui.core.LoadingOverlay

@Composable
fun LoginScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val state = viewModel.state

    var identifier by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(state.isLoggedIn) {
        if (state.isLoggedIn) {
            onLoginSuccess()
        }
    }

    if (state.isLoading) {
        LoadingOverlay()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppHeader()

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = stringResource(R.string.login_title),
                fontSize = 32.sp,
                color = CloudWhite,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(32.dp))

            TextField(
                value = identifier,
                onValueChange = { identifier = it },
                label = stringResource(R.string.label_email_or_username),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                label = stringResource(R.string.label_password),
                isPassword = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                text = stringResource(R.string.action_login),
                onClick = { viewModel.login(identifier, password) },
                isLoading = state.isLoading,
                modifier = Modifier.fillMaxWidth(),
                containerColor = CyanNeon.copy(alpha = 0.5f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = onNavigateToRegister) {
                Text(
                    stringResource(R.string.link_no_account_register),
                    color = CyanNeon,
                    fontWeight = FontWeight.Medium
                )
            }

            state.isLoading.let {
                if (it) {
                    Spacer(modifier = Modifier.height(16.dp))
                    CircularProgressIndicator(color = CyanNeon)
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}
