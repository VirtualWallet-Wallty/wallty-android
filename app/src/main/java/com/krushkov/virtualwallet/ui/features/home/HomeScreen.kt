package com.krushkov.virtualwallet.ui.features.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.key
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import java.math.BigDecimal
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.krushkov.virtualwallet.R
import com.krushkov.virtualwallet.ui.core.*
import com.krushkov.virtualwallet.ui.features.home.components.HeroWalletSection
import com.krushkov.virtualwallet.ui.features.transactions.components.LatestTransactionsSection
import com.krushkov.virtualwallet.ui.features.transactions.components.TransactionsSection
import com.krushkov.virtualwallet.ui.nav.Routes
import com.krushkov.virtualwallet.ui.theme.*
import com.krushkov.virtualwallet.ui.utils.innerShadow
import com.krushkov.virtualwallet.ui.utils.outerShadow
import com.krushkov.virtualwallet.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    onPayModeVisibilityChanged: (Boolean) -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val focusManager = LocalFocusManager.current
    val hasWallets = state.wallets.isNotEmpty()

    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.refreshAll()
        }
    }

    LaunchedEffect(hasWallets, state.wallet?.id, state.isAllWalletsVisible) {
        onPayModeVisibilityChanged(hasWallets && state.wallet != null && !state.isAllWalletsVisible)
    }

    DisposableEffect(Unit) {
        onDispose { onPayModeVisibilityChanged(false) }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (state.editingWalletId != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        focusManager.clearFocus()
                        viewModel.commitWalletNameEdit()
                    }
            )
        }

        Scaffold(
            topContent = {
                state.wallet?.let { wallet ->
                    HeroWalletSection(
                        wallet = wallet,
                        walletCount = state.wallets.size,
                        cardCount = state.cardCount,
                        isAllWalletsVisible = state.isAllWalletsVisible,
                        isEditingWallets = state.isEditingWallets,
                        onToggleAllWallets = { viewModel.toggleAllWallets(it) },
                        onToggleEditingWallets = { viewModel.toggleEditingWallets() },
                        onCancelEditingWallets = { viewModel.cancelEditingWallets() },
                        onTopUpClick = { navController.navigate("top_up/wallet/${wallet.id}") },
                        onAddCardClick = { navController.navigate(Routes.ADD_CARD) },
                        onMoveClick = { navController.navigate("move/${wallet.id}") },
                        onNewWalletClick = { navController.navigate(Routes.CREATE_WALLET) },
                        onTransferClick = { navController.navigate(Routes.TRANSFER) },
                        currencies = state.currencies
                    )
                } ?: NewWalletHomeAction(
                    onNewWalletClick = { navController.navigate(Routes.CREATE_WALLET) }
                )
            },
            cardTitle = if (state.isAllWalletsVisible) null else stringResource(R.string.title_latest_transactions),
            showCardBackground = !state.isAllWalletsVisible,
            cardContentScrollable = state.isAllWalletsVisible,
            cardContent = {
                if (!hasWallets) {
                    EmptyWalletHomeContent(
                        emptyMessage = stringResource(R.string.msg_no_transactions)
                    )
                } else if (state.isAllWalletsVisible) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        state.wallets.forEach { wallet ->
                            key(wallet.id) {
                                val currencySymbol = wallet.currency?.symbol ?: state.currencies[wallet.currencyCode]?.symbol ?: wallet.currencyCode ?: ""
                                val balanceText = "${wallet.balance} $currencySymbol"
                                val isNameEditing = state.isEditingWallets && state.editingWalletId == wallet.id
                                val canDelete = !wallet.isDefault
                                        && wallet.balance.compareTo(BigDecimal.ZERO) == 0
                                        && state.wallets.size > 1
                                val focusRequester = remember { FocusRequester() }

                                LaunchedEffect(isNameEditing) {
                                    if (isNameEditing) runCatching { focusRequester.requestFocus() }
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(56.dp)
                                            .outerShadow(AppButtonShape)
                                            .clip(AppButtonShape)
                                            .clickable(
                                                interactionSource = remember { MutableInteractionSource() },
                                                indication = null
                                            ) {
                                                if (state.isEditingWallets) viewModel.startEditingWalletName(wallet)
                                                else viewModel.selectWallet(wallet)
                                            }
                                    ) {
                                        Box(modifier = Modifier.matchParentSize().innerShadow(AppButtonShape)) {
                                            Box(modifier = Modifier.fillMaxSize().background(CyanNeon.copy(alpha = 0.3f)))
                                        }
                                        Box(modifier = Modifier.matchParentSize().border(AppBorderStroke, AppButtonShape))

                                        val pillState = when {
                                            isNameEditing -> 2
                                            state.isEditingWallets -> 1
                                            else -> 0
                                        }
                                        AnimatedContent(
                                            targetState = pillState,
                                            transitionSpec = { fadeIn(tween(250)) togetherWith fadeOut(tween(250)) },
                                            label = "WalletPill"
                                        ) { ps ->
                                            when (ps) {
                                                2 -> Row(
                                                    modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                                ) {
                                                    BasicTextField(
                                                        value = state.editingWalletName,
                                                        onValueChange = { viewModel.onEditingWalletNameChange(it) },
                                                        textStyle = TextStyle(color = CloudWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold),
                                                        singleLine = true,
                                                        cursorBrush = SolidColor(CloudWhite),
                                                        modifier = Modifier.weight(1f).focusRequester(focusRequester)
                                                    )
                                                    Text("⋅ $balanceText", color = CloudWhite.copy(alpha = 0.6f), fontSize = 13.sp)
                                                }
                                                1 -> Row(
                                                    modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.SpaceBetween
                                                ) {
                                                    Text(wallet.name, color = CloudWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                                    Text("⋅ $balanceText", color = CloudWhite.copy(alpha = 0.6f), fontSize = 13.sp)
                                                }
                                                else -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                                    Text(
                                                        text = "${wallet.name} ⋅ $balanceText",
                                                        color = CloudWhite,
                                                        fontSize = 14.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        textAlign = TextAlign.Center
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    AnimatedVisibility(
                                        visible = state.isEditingWallets,
                                        enter = expandHorizontally(tween(300)) + fadeIn(tween(250)),
                                        exit = shrinkHorizontally(tween(300)) + fadeOut(tween(250))
                                    ) {
                                        val isDefault = wallet.isDefault
                                        val isPendingDefault = state.pendingDefaultWalletId == wallet.id
                                        val showAsDefault = (isDefault && state.pendingDefaultWalletId == null) || isPendingDefault

                                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                            Button(
                                                onClick = {
                                                    if (!showAsDefault) {
                                                        viewModel.setPendingDefaultWallet(wallet.id)
                                                    }
                                                },
                                                modifier = Modifier
                                                    .width(110.dp)
                                                    .height(40.dp)
                                                    .outerShadow(AppButtonShape),
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = Color.Transparent,
                                                    contentColor = CloudWhite
                                                ),
                                                shape = AppButtonShape,
                                                contentPadding = PaddingValues(0.dp)
                                            ) {
                                                Box(
                                                    modifier = Modifier.fillMaxSize(),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Box(
                                                        modifier = Modifier
                                                            .fillMaxSize()
                                                            .clip(AppButtonShape)
                                                            .innerShadow(AppButtonShape)
                                                    ) {
                                                        Box(
                                                            modifier = Modifier
                                                                .fillMaxSize()
                                                                .background(
                                                                    if (showAsDefault) Color(0xFFDCB519).copy(alpha = 0.8f)
                                                                    else CyanNeon.copy(alpha = 0.2f)
                                                                )
                                                        )
                                                    }
                                                    Box(
                                                        modifier = Modifier
                                                            .fillMaxSize()
                                                            .border(AppBorderStroke, AppButtonShape)
                                                    )
                                                    Row(
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        horizontalArrangement = Arrangement.Center,
                                                        modifier = Modifier
                                                            .fillMaxSize()
                                                            .padding(horizontal = 8.dp)
                                                    ) {
                                                        Icon(
                                                            imageVector = if (showAsDefault) Icons.Default.Star else Icons.Outlined.StarBorder,
                                                            contentDescription = null,
                                                            modifier = Modifier.size(20.dp),
                                                            tint = CloudWhite
                                                        )
                                                        Spacer(modifier = Modifier.width(6.dp))
                                                        val defaultLabel = stringResource(R.string.action_default)
                                                        var defaultLabelSize by remember(defaultLabel) { mutableStateOf(13.sp) }
                                                        Text(
                                                            text = defaultLabel,
                                                            fontSize = defaultLabelSize,
                                                            fontWeight = FontWeight.Medium,
                                                            maxLines = 1,
                                                            softWrap = false,
                                                            onTextLayout = { result ->
                                                                if (result.hasVisualOverflow && defaultLabelSize.value > 9f) {
                                                                    defaultLabelSize = (defaultLabelSize.value - 0.5f).coerceAtLeast(9f).sp
                                                                }
                                                            }
                                                        )
                                                    }
                                                }
                                            }

                                            CircleButton(
                                                icon = {
                                                    Icon(
                                                        imageVector = Icons.Default.Delete,
                                                        contentDescription = null,
                                                        tint = CloudWhite,
                                                        modifier = Modifier.size(20.dp)
                                                    )
                                                },
                                                onClick = { viewModel.deleteWallet(wallet.id) },
                                                containerColor = if (canDelete) Red.copy(alpha = 0.5f) else Black.copy(alpha = 0.4f),
                                                enabled = canDelete
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }

                        if (state.isEditingWallets && state.wallets.size < 5) {
                            Button(
                                text = stringResource(R.string.action_new_wallet),
                                onClick = { navController.navigate(Routes.CREATE_WALLET) },
                                containerColor = CyanNeon.copy(alpha = 0.5f),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                } else {
                    state.wallet?.let { wallet ->
                        LatestTransactionsSection(
                            transactions = state.transactions,
                            currentWalletId = wallet.id,
                            currencies = state.currencies,
                            emptyMessage = stringResource(R.string.msg_no_transactions),
                            seeAllText = stringResource(R.string.action_see_all),
                            onSeeAllClick = { navController.navigate("transactions?walletId=${wallet.id}") },
                            onTransactionClick = { navController.navigate(Routes.transactionDetails(it.id)) }
                        )
                    }
                }
            }
        )
    }
}

@Composable
private fun NewWalletHomeAction(
    onNewWalletClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 20.dp, end = 20.dp)
    ) {
        Button(
            text = stringResource(R.string.action_new_wallet),
            onClick = onNewWalletClick,
            containerColor = CyanNeon.copy(alpha = 0.5f),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun EmptyWalletHomeContent(
    emptyMessage: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = emptyMessage,
            color = CloudWhite.copy(alpha = 0.5f),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 32.dp)
        )
    }
}
