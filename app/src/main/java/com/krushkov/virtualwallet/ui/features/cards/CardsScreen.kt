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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.krushkov.virtualwallet.R
import com.krushkov.virtualwallet.ui.core.*
import com.krushkov.virtualwallet.ui.nav.Routes
import com.krushkov.virtualwallet.ui.features.cards.components.CardsHeroSection
import com.krushkov.virtualwallet.ui.features.cards.components.CardsHeroSectionShimmer
import com.krushkov.virtualwallet.ui.features.transactions.components.LatestTransactionsSection
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
                        onRemoveClick = { viewModel.showRemoveConfirm() },
                        onAddCardClick = { navController.navigate(Routes.ADD_CARD) }
                    )
                }
            },
            cardTitle = stringResource(R.string.title_latest_topups),
            showCardBackground = true,
            cardContentScrollable = false,
            cardContent = {
                state.selectedCard?.let { card ->
                    LatestTransactionsSection(
                        transactions = state.topUps,
                        currentWalletId = -1,
                        currencies = state.currencies,
                        emptyMessage = stringResource(R.string.msg_no_topups),
                        seeAllText = stringResource(R.string.action_see_all),
                        onSeeAllClick = {
                            navController.navigate(
                                "transactions?type=TOP_UP&cardId=${card.id}&label=${card.cardSuffix}"
                            )
                        },
                        onTransactionClick = { navController.navigate(Routes.transactionDetails(it.id)) }
                    )
                } ?: LatestTransactionsSection(
                    transactions = state.topUps,
                    currentWalletId = -1,
                    currencies = state.currencies,
                    emptyMessage = stringResource(R.string.msg_no_topups),
                    seeAllText = stringResource(R.string.action_see_all),
                    onSeeAllClick = {},
                    onTransactionClick = { navController.navigate(Routes.transactionDetails(it.id)) }
                )
            }
        )
    }
}
