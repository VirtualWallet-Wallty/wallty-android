package com.krushkov.virtualwallet.ui.features.auth

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
import androidx.hilt.navigation.compose.hiltViewModel
import com.krushkov.virtualwallet.ui.common.AppHeader
import com.krushkov.virtualwallet.ui.core.Button
import com.krushkov.virtualwallet.ui.core.TextField
import com.krushkov.virtualwallet.ui.theme.*
import com.krushkov.virtualwallet.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onRegisterSuccess: () -> Unit,
    onBack: () -> Unit
) {
    val state = viewModel.state

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    LaunchedEffect(state.isRegistered) {
        if (state.isRegistered) {
            onRegisterSuccess()
        }
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
                text = "Register",
                fontSize = 32.sp,
                color = CloudWhite,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(32.dp))

            TextField(
                value = username,
                onValueChange = { username = it },
                label = "Username",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = email,
                onValueChange = { email = it },
                label = "Email",
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = "First Name",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = "Last Name",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                isPassword = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                text = "Register",
                onClick = {
                    viewModel.register(username, password, firstName, lastName, email)
                },
                isLoading = state.isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = onBack) {
                Text(
                    "Back to login",
                    color = CyanNeon,
                    fontWeight = FontWeight.Medium
                )
            }

            if (state.isLoading) {
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator(color = CyanNeon)
            }
            
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}
