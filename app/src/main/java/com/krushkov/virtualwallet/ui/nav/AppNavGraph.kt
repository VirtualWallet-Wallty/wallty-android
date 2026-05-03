package com.krushkov.virtualwallet.ui.nav

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.navigation.NavType
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.krushkov.virtualwallet.R
import com.krushkov.virtualwallet.ui.core.AppBackgroundStyle
import com.krushkov.virtualwallet.ui.core.CircleButton
import com.krushkov.virtualwallet.ui.theme.CyanNeon
import com.krushkov.virtualwallet.ui.theme.Green
import com.krushkov.virtualwallet.ui.theme.LocalSetAppBackgroundStyle
import com.krushkov.virtualwallet.ui.theme.NightBlack
import com.krushkov.virtualwallet.ui.theme.Yellow
import com.krushkov.virtualwallet.ui.theme.AppBorderStroke
import com.krushkov.virtualwallet.ui.theme.AppButtonShape
import com.krushkov.virtualwallet.ui.theme.CloudWhite
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
import com.krushkov.virtualwallet.ui.features.payment.PaymentConfirmScreen
import com.krushkov.virtualwallet.ui.features.pos.PosTerminalScreen
import com.krushkov.virtualwallet.ui.features.transfer.TransferScreen
import com.krushkov.virtualwallet.ui.features.transfer.SendConfirmScreen
import com.krushkov.virtualwallet.ui.features.topup.TopUpScreen
import com.krushkov.virtualwallet.ui.features.move.MoveScreen
import com.krushkov.virtualwallet.viewmodel.TransferViewModel
import com.krushkov.virtualwallet.ui.features.settings.SettingsScreen
import com.krushkov.virtualwallet.ui.features.transactions.TransactionsScreen
import com.krushkov.virtualwallet.ui.features.transactions.TransactionDetailsScreen
import com.krushkov.virtualwallet.viewmodel.AppViewModel
import com.krushkov.virtualwallet.viewmodel.AuthViewModel
import com.krushkov.virtualwallet.ui.utils.innerShadow
import com.krushkov.virtualwallet.ui.utils.outerShadow

@Composable
fun AppNavGraph(
    authViewModel: AuthViewModel = hiltViewModel(),
    appViewModel: AppViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val state = authViewModel.state
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val setBackgroundStyle = LocalSetAppBackgroundStyle.current

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

    val showChrome = currentRoute?.substringBefore("?") in screenOrder

    LaunchedEffect(currentRoute) {
        setBackgroundStyle(backgroundStyleForRoute(currentRoute))
    }

    LaunchedEffect(state.isLoggedIn, currentRoute) {
        if (state.isLoggedIn && currentRoute?.substringBefore("?") == Routes.HOME) {
            appViewModel.refreshWalletAvailability()
        }
    }

    LaunchedEffect(state.isLoggedIn) {
        if (!state.isLoggedIn && !state.isCheckingSession) {
            navController.navigate(Routes.LOGIN) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    LaunchedEffect(appViewModel.navigateToPaymentConfirm) {
        if (appViewModel.navigateToPaymentConfirm) {
            appViewModel.onPaymentConfirmNavigated()
            navController.navigate(Routes.PAYMENT_CONFIRM)
        }
    }

    if (state.isCheckingSession) {
        LoadingOverlay()
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize().systemBarsPadding()) {
                if (showChrome) {
                    Spacer(modifier = Modifier.height(16.dp))
                    HeaderWithPayMode(
                        showPayMode = currentRoute == Routes.HOME && appViewModel.hasWallets && appViewModel.isHomeNormalMode,
                        isPayModeActive = appViewModel.isPayModeActive,
                        onPayModeClick = { appViewModel.togglePayMode() },
                        onDebugClick = { navController.navigate(Routes.POS_TERMINAL) }
                    )
                }

                Box(modifier = Modifier.weight(1f)) {
                    NavHost(
                        navController = navController,
                        startDestination = if (state.isLoggedIn) Routes.HOME else Routes.LOGIN,
                        enterTransition = {
                            val initialStateName = initialState.destination.route
                            val targetStateName = targetState.destination.route
                            
                            val initialIndex = screenOrder.indexOf(initialStateName?.substringBefore("?"))
                            val targetIndex = screenOrder.indexOf(targetStateName?.substringBefore("?"))

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
                            
                            val initialIndex = screenOrder.indexOf(initialStateName?.substringBefore("?"))
                            val targetIndex = screenOrder.indexOf(targetStateName?.substringBefore("?"))

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
                            
                            val initialIndex = screenOrder.indexOf(initialStateName?.substringBefore("?"))
                            val targetIndex = screenOrder.indexOf(targetStateName?.substringBefore("?"))

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
                            
                            val initialIndex = screenOrder.indexOf(initialStateName?.substringBefore("?"))
                            val targetIndex = screenOrder.indexOf(targetStateName?.substringBefore("?"))

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
                            HomeScreen(
                                navController = navController,
                                onPayModeVisibilityChanged = appViewModel::updateHomeNormalMode
                            )
                        }

                        composable(Routes.CARDS) {
                            CardsScreen(navController)
                        }

                        composable(
                            route = Routes.TRANSACTIONS_FULL,
                            arguments = listOf(
                                navArgument("walletId") { type = NavType.LongType; defaultValue = -1L },
                                navArgument("type") { type = NavType.StringType; nullable = true; defaultValue = null },
                                navArgument("cardId") { type = NavType.LongType; defaultValue = -1L },
                                navArgument("label") { type = NavType.StringType; nullable = true; defaultValue = null }
                            )
                        ) {
                            TransactionsScreen(navController)
                        }

                        composable(
                            route = Routes.TRANSACTION_DETAILS,
                            arguments = listOf(navArgument("transactionId") { type = NavType.LongType })
                        ) {
                            TransactionDetailsScreen(navController)
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

                        composable(Routes.PAYMENT_CONFIRM) {
                            PaymentConfirmScreen(navController)
                        }

                        composable(Routes.POS_TERMINAL) {
                            PosTerminalScreen(navController)
                        }

                        navigation(
                            startDestination = Routes.TRANSFER,
                            route = Routes.SEND_FLOW
                        ) {
                            composable(Routes.TRANSFER) { entry ->
                                val parentEntry = remember(entry) {
                                    navController.getBackStackEntry(Routes.SEND_FLOW)
                                }
                                val viewModel: TransferViewModel = hiltViewModel(parentEntry)
                                TransferScreen(navController, viewModel)
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

@Composable
private fun HeaderWithPayMode(
    showPayMode: Boolean,
    isPayModeActive: Boolean,
    onPayModeClick: () -> Unit,
    onDebugClick: () -> Unit
) {
    val nfcIcon = remember {
        ImageVector.Builder(
            name = "Nfc",
            defaultWidth = 20.dp,
            defaultHeight = 20.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(
            stroke = SolidColor(Color.White),
            strokeLineWidth = 1.5f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round
        ) {
            moveTo(12f, 12f)
            arcTo(1f, 1f, 0f, false, true, 13f, 12f)
            arcTo(1f, 1f, 0f, false, true, 12f, 12f)
            close()
            moveTo(8.5f, 8.5f)
            arcTo(5f, 5f, 0f, false, true, 15.5f, 15.5f)
            moveTo(5.5f, 5.5f)
            arcTo(9f, 9f, 0f, false, true, 18.5f, 18.5f)
        }.build()
    }

    Box(modifier = Modifier.fillMaxWidth().height(32.dp)) {
        AppHeader()

        androidx.compose.animation.AnimatedVisibility(
            visible = showPayMode,
            enter = fadeIn(tween(180)),
            exit = fadeOut(tween(120)),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = 42.dp)
        ) {
            Box {
                PayModeHeaderButton(
                    isActive = isPayModeActive,
                    icon = {
                        Icon(
                            painter = rememberVectorPainter(nfcIcon),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    onClick = onPayModeClick
                )

                androidx.compose.animation.AnimatedVisibility(
                    visible = isPayModeActive,
                    enter = fadeIn(tween(180)),
                    exit = fadeOut(tween(120)),
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .offset(y = 40.dp)
                        .alpha(0f)
                ) {
                    CircleButton(
                        icon = {
                            Text(text = "⚡", fontSize = 12.sp)
                        },
                        onClick = onDebugClick,
                        containerColor = Yellow.copy(alpha = 0.4f)
                    )
                }
            }
        }
    }
}

@Composable
private fun PayModeHeaderButton(
    isActive: Boolean,
    icon: @Composable () -> Unit,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .height(36.dp)
            .widthIn(min = 36.dp)
            .outerShadow(AppButtonShape)
            .clip(AppButtonShape)
            .clickable(onClick = onClick)
            .animateContentSize(animationSpec = tween(durationMillis = 260))
    ) {
        Box(modifier = Modifier.matchParentSize().innerShadow(AppButtonShape)) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        if (isActive) Green.copy(alpha = 0.5f)
                        else CyanNeon.copy(alpha = 0.3f)
                    )
            )
        }
        Box(modifier = Modifier.matchParentSize().border(AppBorderStroke, AppButtonShape))

        Row(
            modifier = Modifier
                .height(36.dp)
                .padding(horizontal = 9.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(modifier = Modifier.size(18.dp), contentAlignment = Alignment.Center) {
                icon()
            }

            AnimatedVisibility(
                visible = isActive,
                enter = expandHorizontally(animationSpec = tween(220)) + fadeIn(tween(220)),
                exit = shrinkHorizontally(animationSpec = tween(180)) + fadeOut(tween(180)),
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Text(
                    text = androidx.compose.ui.res.stringResource(R.string.action_pay_mode_off),
                    color = CloudWhite,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    softWrap = false
                )
            }
        }
    }
}

private fun backgroundStyleForRoute(route: String?): AppBackgroundStyle {
    return when (route?.substringBefore("?")) {
        Routes.TRANSACTION_DETAILS,
        Routes.PAYMENT_CONFIRM,
        Routes.POS_TERMINAL -> AppBackgroundStyle.Solid(NightBlack)
        else -> AppBackgroundStyle.Signature
    }
}
