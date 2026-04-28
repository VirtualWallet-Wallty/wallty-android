package com.krushkov.virtualwallet.ui.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.krushkov.virtualwallet.domain.models.outputs.transaction.Transaction
import com.krushkov.virtualwallet.ui.core.*
import com.krushkov.virtualwallet.ui.features.home.components.HeroWalletSection
import com.krushkov.virtualwallet.ui.features.transactions.components.TransactionDetailDialog
import com.krushkov.virtualwallet.ui.features.transactions.components.TransactionsSection
import com.krushkov.virtualwallet.ui.nav.Routes
import com.krushkov.virtualwallet.ui.theme.*
import com.krushkov.virtualwallet.ui.utils.innerShadow
import com.krushkov.virtualwallet.ui.utils.outerShadow
import com.krushkov.virtualwallet.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state = viewModel.state
    var selectedTransaction by remember { mutableStateOf<Transaction?>(null) }

    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.refreshAll()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
                    topContent = {
                        state.wallet?.let { wallet ->
                            HeroWalletSection(
                                wallet = wallet,
                                isAllWalletsVisible = state.isAllWalletsVisible,
                                isEditingWallets = state.isEditingWallets,
                                onToggleAllWallets = { viewModel.toggleAllWallets(it) },
                                onToggleEditingWallets = { viewModel.toggleEditingWallets() },
                                onCancelEditingWallets = { viewModel.cancelEditingWallets() },
                                onTopUpClick = { state.wallet?.id?.let { navController.navigate("top_up/wallet/$it") } },
                                onMoveClick = { state.wallet?.id?.let { navController.navigate("move/$it") } },
                onTransferClick = { navController.navigate(Routes.TRANSFER) },
                                currencies = state.currencies
                            )
                        }
                    },
                    cardTitle = if (state.isAllWalletsVisible) null else "Latest transactions",
                    showCardBackground = !state.isAllWalletsVisible,
                    cardContent = {
                        if (state.isAllWalletsVisible) {
                            state.wallets.forEach { wallet ->
                                val currencySymbol = wallet.currency?.symbol ?: state.currencies[wallet.currencyCode]?.symbol ?: wallet.currencyCode ?: ""
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Button(
                                        text = "${wallet.name} - ${wallet.balance} $currencySymbol",
                                        onClick = {
                                            if (!state.isEditingWallets) {
                                                viewModel.selectWallet(wallet)
                                            }
                                        },
                                        containerColor = CyanNeon.copy(alpha = 0.3f),
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(56.dp)
                                    )

                                    if (state.isEditingWallets) {
                                        val isDefault = wallet.isDefault
                                        val isPendingDefault = state.pendingDefaultWalletId == wallet.id
                                        val showAsDefault = (isDefault && state.pendingDefaultWalletId == null) || isPendingDefault

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
                                                    Text(
                                                        text = "Default",
                                                        fontSize = 13.sp,
                                                        fontWeight = FontWeight.Medium
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                            }

                            if (state.isEditingWallets && state.wallets.size < 5) {
                                Button(
                                    text = "New Wallet",
                                    onClick = { navController.navigate(Routes.CREATE_WALLET) },
                                    containerColor = CyanNeon.copy(alpha = 0.5f),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        } else {
                            state.wallet?.let { wallet ->
                                TransactionsSection(
                                    transactions = state.transactions,
                                    currentWalletId = wallet.id,
                                    currencies = state.currencies,
                                    emptyMessage = "No transactions yet for this wallet.",
                                    onTransactionClick = { selectedTransaction = it }
                                )
                            }
                        }
                    }
                )

            selectedTransaction?.let { transaction ->
                state.wallet?.let { wallet ->
                    TransactionDetailDialog(
                        transaction = transaction,
                        currentWalletId = wallet.id,
                        currencies = state.currencies,
                        onDismiss = { selectedTransaction = null }
                    )
                }
        }
    }
}
