package com.krushkov.virtualwallet.ui.features.settings

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.krushkov.virtualwallet.ui.core.Button
import com.krushkov.virtualwallet.ui.core.Scaffold
import com.krushkov.virtualwallet.ui.theme.Red
import com.krushkov.virtualwallet.viewmodel.AuthViewModel

@Composable
fun SettingsScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    Scaffold(
        cardTitle = "Settings",
        cardContent = {
            Button(
                text = "Logout",
                onClick = { authViewModel.logout() },
                modifier = Modifier.fillMaxWidth(),
                containerColor = Red.copy(alpha = 0.5f)
            )
        }
    )
}
