package com.krushkov.virtualwallet.ui.navigation

import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.compose.*
import com.krushkov.virtualwallet.ui.screens.*
import com.krushkov.virtualwallet.viewmodel.*

@Composable
fun AppNavGraph(
    authViewModel: AuthViewModel,
    walletViewModel: WalletViewModel
) {

    val state by authViewModel.authState.observeAsState(AuthState.Loading)
    val navController = rememberNavController()

    LaunchedEffect(Unit) {
        authViewModel.checkSession()
    }

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {

        composable("splash") {
            SplashScreen()
        }

        composable("login") {
            LoginScreen(authViewModel, navController)
        }

        composable("home") {
            WalletListScreen(walletViewModel, navController)
        }

        composable("wallet/{walletId}") { backStackEntry ->

            val walletId = backStackEntry.arguments
                ?.getString("walletId")
                ?.toLongOrNull() ?: return@composable

            WalletDetailsScreen(walletId, walletViewModel)
        }
    }

    LaunchedEffect(state) {
        when (state) {

            is AuthState.Authenticated -> {
                navController.navigate("home") {
                    popUpTo("splash") { inclusive = true }
                    launchSingleTop = true
                }
            }

            is AuthState.Unauthenticated -> {
                navController.navigate("login") {
                    popUpTo("splash") { inclusive = true }
                    launchSingleTop = true
                }
            }

            else -> Unit
        }
    }
}