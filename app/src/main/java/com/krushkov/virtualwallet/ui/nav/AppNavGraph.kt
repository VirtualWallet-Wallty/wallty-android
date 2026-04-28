package com.krushkov.virtualwallet.ui.nav

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavType
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.krushkov.virtualwallet.ui.common.AppHeader
import com.krushkov.virtualwallet.ui.common.AppNotificationHost
import com.krushkov.virtualwallet.ui.common.BottomNavBar
import com.krushkov.virtualwallet.ui.common.NotificationData
import com.krushkov.virtualwallet.ui.core.*
import com.krushkov.virtualwallet.ui.features.auth.LoginScreen
import com.krushkov.virtualwallet.ui.features.auth.RegisterScreen
import com.krushkov.virtualwallet.ui.features.cards.CardsScreen
import com.krushkov.virtualwallet.ui.features.cards.AddCardScreen
import com.krushkov.virtualwallet.ui.features.home.CreateWalletScreen
import com.krushkov.virtualwallet.ui.features.home.HomeScreen
import com.krushkov.virtualwallet.ui.features.transfer.TransferScreen
import com.krushkov.virtualwallet.ui.features.transfer.ReceiveScreen
import com.krushkov.virtualwallet.ui.features.transfer.SendScreen
import com.krushkov.virtualwallet.ui.features.transfer.SendConfirmScreen
import com.krushkov.virtualwallet.ui.features.topup.TopUpScreen
import com.krushkov.virtualwallet.ui.features.move.MoveScreen
import com.krushkov.virtualwallet.viewmodel.TransferViewModel
import com.krushkov.virtualwallet.ui.features.settings.SettingsScreen
import com.krushkov.virtualwallet.ui.features.transactions.TransactionsScreen
import com.krushkov.virtualwallet.viewmodel.AppViewModel
import com.krushkov.virtualwallet.viewmodel.AuthViewModel

@Composable
fun AppNavGraph(
    authViewModel: AuthViewModel = hiltViewModel(),
    appViewModel: AppViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val state = authViewModel.state
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    var notification by remember { mutableStateOf<NotificationData?>(null) }

    LaunchedEffect(Unit) {
        appViewModel.notificationManager.notifications.collect {
            notification = it
        }
    }

    val screenOrder = listOf(
        Routes.HOME,
        Routes.CARDS,
        Routes.TRANSACTIONS,
        Routes.SETTINGS
    )

    val showChrome = currentRoute in screenOrder

    LaunchedEffect(state.isLoggedIn) {
        if (!state.isLoggedIn && !state.isCheckingSession) {
            navController.navigate(Routes.LOGIN) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    if (state.isCheckingSession) {
        LoadingOverlay()
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize().systemBarsPadding()) {
                if (showChrome) {
                    Spacer(modifier = Modifier.height(16.dp))
                    AppHeader()
                }

                Box(modifier = Modifier.weight(1f)) {
                    NavHost(
                        navController = navController,
                        startDestination = if (state.isLoggedIn) Routes.HOME else Routes.LOGIN,
                        enterTransition = {
                            val initialStateName = initialState.destination.route
                            val targetStateName = targetState.destination.route
                            
                            val initialIndex = screenOrder.indexOf(initialStateName)
                            val targetIndex = screenOrder.indexOf(targetStateName)

                            if (initialIndex != -1 && targetIndex != -1) {
                                if (targetIndex > initialIndex) {
                                    slideIntoContainer(
                                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                        animationSpec = tween(400)
                                    ) + fadeIn(animationSpec = tween(400))
                                } else {
                                    slideIntoContainer(
                                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                                        animationSpec = tween(400)
                                    ) + fadeIn(animationSpec = tween(400))
                                }
                            } else {
                                fadeIn(animationSpec = tween(400))
                            }
                        },
                        exitTransition = {
                            val initialStateName = initialState.destination.route
                            val targetStateName = targetState.destination.route
                            
                            val initialIndex = screenOrder.indexOf(initialStateName)
                            val targetIndex = screenOrder.indexOf(targetStateName)

                            if (initialIndex != -1 && targetIndex != -1) {
                                if (targetIndex > initialIndex) {
                                    slideOutOfContainer(
                                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                        animationSpec = tween(400)
                                    ) + fadeOut(animationSpec = tween(400))
                                } else {
                                    slideOutOfContainer(
                                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                                        animationSpec = tween(400)
                                    ) + fadeOut(animationSpec = tween(400))
                                }
                            } else {
                                fadeOut(animationSpec = tween(400))
                            }
                        },
                        popEnterTransition = {
                            val initialStateName = initialState.destination.route
                            val targetStateName = targetState.destination.route
                            
                            val initialIndex = screenOrder.indexOf(initialStateName)
                            val targetIndex = screenOrder.indexOf(targetStateName)

                            if (initialIndex != -1 && targetIndex != -1) {
                                if (targetIndex > initialIndex) {
                                    slideIntoContainer(
                                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                        animationSpec = tween(400)
                                    ) + fadeIn(animationSpec = tween(400))
                                } else {
                                    slideIntoContainer(
                                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                                        animationSpec = tween(400)
                                    ) + fadeIn(animationSpec = tween(400))
                                }
                            } else {
                                fadeIn(animationSpec = tween(400))
                            }
                        },
                        popExitTransition = {
                            val initialStateName = initialState.destination.route
                            val targetStateName = targetState.destination.route
                            
                            val initialIndex = screenOrder.indexOf(initialStateName)
                            val targetIndex = screenOrder.indexOf(targetStateName)

                            if (initialIndex != -1 && targetIndex != -1) {
                                if (targetIndex > initialIndex) {
                                    slideOutOfContainer(
                                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                        animationSpec = tween(400)
                                    ) + fadeOut(animationSpec = tween(400))
                                } else {
                                    slideOutOfContainer(
                                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                                        animationSpec = tween(400)
                                    ) + fadeOut(animationSpec = tween(400))
                                }
                            } else {
                                fadeOut(animationSpec = tween(400))
                            }
                        }
                    ) {
                        composable(Routes.LOGIN) {
                            LoginScreen(
                                viewModel = authViewModel,
                                onLoginSuccess = {
                                    navController.navigate(Routes.HOME) {
                                        popUpTo(Routes.LOGIN) { inclusive = true }
                                    }
                                },
                                onNavigateToRegister = {
                                    navController.navigate(Routes.REGISTER)
                                }
                            )
                        }

                        composable(Routes.REGISTER) {
                            RegisterScreen(
                                viewModel = authViewModel,
                                onRegisterSuccess = {
                                    authViewModel.resetRegistrationState()
                                    navController.popBackStack()
                                },
                                onBack = { navController.popBackStack() }
                            )
                        }

                        composable(Routes.HOME) {
                            HomeScreen(navController)
                        }

                        composable(Routes.CARDS) {
                            CardsScreen(navController)
                        }

                        composable(Routes.TRANSACTIONS) {
                            TransactionsScreen(navController)
                        }

                        composable(Routes.SETTINGS) {
                            SettingsScreen(
                                navController = navController,
                                authViewModel = authViewModel
                            )
                        }

                        composable(Routes.CREATE_WALLET) {
                            CreateWalletScreen(navController)
                        }

                        composable(Routes.ADD_CARD) {
                            AddCardScreen(navController)
                        }

                        composable(Routes.TRANSFER) {
                            TransferScreen(navController)
                        }

                        composable(Routes.RECEIVE) {
                            ReceiveScreen(navController)
                        }

                        composable(
                            route = Routes.TOP_UP,
                            arguments = listOf(
                                navArgument("mode") { type = NavType.StringType },
                                navArgument("id") { type = NavType.LongType }
                            )
                        ) {
                            TopUpScreen(navController)
                        }

                        composable(
                            route = Routes.MOVE,
                            arguments = listOf(
                                navArgument("walletId") { type = NavType.LongType }
                            )
                        ) {
                            MoveScreen(navController)
                        }

                        navigation(
                            startDestination = Routes.SEND,
                            route = Routes.SEND_FLOW
                        ) {
                            composable(Routes.SEND) { entry ->
                                val parentEntry = remember(entry) {
                                    navController.getBackStackEntry(Routes.SEND_FLOW)
                                }
                                val viewModel: TransferViewModel = hiltViewModel(parentEntry)
                                SendScreen(navController, viewModel)
                            }
                            composable(Routes.SEND_CONFIRM) { entry ->
                                val parentEntry = remember(entry) {
                                    navController.getBackStackEntry(Routes.SEND_FLOW)
                                }
                                val viewModel: TransferViewModel = hiltViewModel(parentEntry)
                                SendConfirmScreen(navController, viewModel)
                            }
                        }
                    }
                }
                if (showChrome) {
                    BottomNavBar(navController)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            AppNotificationHost(
                notification = notification,
                onDismiss = { notification = null }
            )
        }
    }
}
