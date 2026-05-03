package com.krushkov.virtualwallet.ui.features.transactions

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.krushkov.virtualwallet.R
import com.krushkov.virtualwallet.domain.models.inputs.transaction.TransactionDirection
import com.krushkov.virtualwallet.domain.models.inputs.transaction.TransactionSortOrder
import com.krushkov.virtualwallet.ui.core.Scaffold
import com.krushkov.virtualwallet.ui.features.transactions.components.TransactionsFilterPanel
import com.krushkov.virtualwallet.ui.features.transactions.components.TransactionsListContent
import com.krushkov.virtualwallet.ui.nav.Routes
import com.krushkov.virtualwallet.ui.theme.CloudWhite
import com.krushkov.virtualwallet.viewmodel.TransactionsViewModel
import com.krushkov.virtualwallet.viewmodel.states.TransactionsState

@Composable
fun TransactionsScreen(
    navController: NavController,
    viewModel: TransactionsViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val selectedWallet = state.wallets.find { it.id == state.currentWalletId }
    val selectedCard = state.cards.find { it.id == state.filterCardId }
    val ownedWalletIds = state.wallets.map { it.id }.toSet()
    val activeFilterCount = state.activeFilterCount()

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topContent = {
                TransactionsTitle()

                Spacer(modifier = Modifier.height(12.dp))

                TransactionsFilterPanel(
                    state = state,
                    activeFilterCount = activeFilterCount,
                    selectedWallet = selectedWallet,
                    selectedCard = selectedCard,
                    onToggleExpanded = viewModel::toggleFilterExpanded,
                    onClearFilters = viewModel::clearFilters,
                    onWalletSelected = viewModel::setSelectedWallet,
                    onCardSelected = viewModel::setFilterCard,
                    onDirectionSelected = viewModel::setFilterDirection,
                    onTypeSelected = viewModel::setFilterType,
                    onSortSelected = viewModel::setSortOrder,
                    onDateFromSelected = viewModel::setFilterDateFrom,
                    onDateToSelected = viewModel::setFilterDateTo
                )
            },
            cardTitle = null,
            showCardBackground = true,
            cardContent = {
                TransactionsListContent(
                    state = state,
                    ownedWalletIds = ownedWalletIds,
                    onLoadNextPage = viewModel::loadNextPage,
                    onTransactionClick = { navController.navigate(Routes.transactionDetails(it)) }
                )
            }
        )
    }
}

@Composable
private fun TransactionsTitle() {
    Text(
        text = stringResource(R.string.title_all_transactions),
        color = CloudWhite,
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    )
}

private fun TransactionsState.activeFilterCount(): Int {
    return listOfNotNull(
        currentWalletId,
        filterDirection.takeIf { it != TransactionDirection.ALL },
        filterType,
        filterCardId,
        filterDateFrom,
        filterDateTo,
        sortOrder.takeIf { it != TransactionSortOrder.NEWEST }
    ).size
}
