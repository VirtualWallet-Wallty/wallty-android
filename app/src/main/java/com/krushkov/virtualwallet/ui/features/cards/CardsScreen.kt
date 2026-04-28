package com.krushkov.virtualwallet.ui.features.cards

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.krushkov.virtualwallet.ui.core.*
import com.krushkov.virtualwallet.ui.nav.Routes
import com.krushkov.virtualwallet.ui.features.cards.components.CardStatusConfirmDialog
import com.krushkov.virtualwallet.ui.features.cards.components.CardsHeroSection
import com.krushkov.virtualwallet.ui.features.cards.components.CardsHeroSectionShimmer
import com.krushkov.virtualwallet.ui.features.transactions.components.TransactionsSection
import com.krushkov.virtualwallet.viewmodel.CardsViewModel

@Composable
fun CardsScreen(
    navController: NavController,
    viewModel: CardsViewModel = hiltViewModel()
) {
    val state = viewModel.state

    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.loadCards()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topContent = {
                if (state.isLoading) {
                    CardsHeroSectionShimmer()
                } else {
                    CardsHeroSection(
                        cards = state.cards,
                        selectedCard = state.selectedCard,
                        onCardSelected = { viewModel.selectCard(it) },
                        onTopUpClick = { state.selectedCard?.id?.let { navController.navigate("top_up/card/$it") } },
                        onCardStatusActionClick = { viewModel.onCardStatusActionClick() },
                        onRemoveClick = { viewModel.removeCard() },
                        onAddCardClick = { navController.navigate(Routes.ADD_CARD) }
                    )
                }
            },
            cardTitle = "Latest Top-ups",
            showCardBackground = true,
            cardContent = {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TransactionsSection(
                        transactions = state.topUps,
                        currentWalletId = -1,
                        currencies = state.currencies,
                        emptyMessage = "No recent top-ups for your cards."
                    )
                }
            }
        )

        if (state.isDeactivateDialogVisible) {
            CardStatusConfirmDialog(
                isDeactivating = true,
                onDismiss = { viewModel.dismissCardStatusDialog() },
                onConfirm = { viewModel.confirmDeactivate() }
            )
        }

        if (state.isActivateDialogVisible) {
            CardStatusConfirmDialog(
                isDeactivating = false,
                onDismiss = { viewModel.dismissCardStatusDialog() },
                onConfirm = { viewModel.confirmActivate() }
            )
        }
    }
}
