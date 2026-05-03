package com.krushkov.virtualwallet.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krushkov.virtualwallet.R
import com.krushkov.virtualwallet.domain.error.getMessage
import com.krushkov.virtualwallet.domain.models.inputs.transaction.TransactionDirection
import com.krushkov.virtualwallet.domain.models.inputs.transaction.TransactionFilterInput
import com.krushkov.virtualwallet.domain.models.inputs.transaction.TransactionSortOrder
import com.krushkov.virtualwallet.domain.models.outputs.transaction.TransactionType
import com.krushkov.virtualwallet.domain.repositories.CardRepository
import com.krushkov.virtualwallet.domain.repositories.CurrencyRepository
import com.krushkov.virtualwallet.domain.repositories.TransactionRepository
import com.krushkov.virtualwallet.domain.repositories.WalletRepository
import com.krushkov.virtualwallet.domain.result.AppResult
import com.krushkov.virtualwallet.ui.utils.NotificationManager
import com.krushkov.virtualwallet.viewmodel.states.TransactionsState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle,
    private val transactionRepository: TransactionRepository,
    private val walletRepository: WalletRepository,
    private val cardRepository: CardRepository,
    private val currencyRepository: CurrencyRepository,
    private val notificationManager: NotificationManager
) : ViewModel() {

    private val initialWalletId: Long? = savedStateHandle.get<Long>("walletId")?.takeIf { it != -1L }
    private val initialType: TransactionType? = savedStateHandle.get<String>("type")
        ?.let { runCatching { TransactionType.valueOf(it) }.getOrNull() }
    private val initialCardId: Long? = savedStateHandle.get<Long>("cardId")?.takeIf { it != -1L }
    private val initialLabel: String? = savedStateHandle.get<String>("label")

    var state by mutableStateOf(
        TransactionsState(
            filterType = initialType,
            filterCardId = initialCardId,
            filterLabel = initialLabel
        )
    )
        private set

    init {
        loadTransactions()
        loadCurrencies()
        loadWallets()
        loadCards()
    }

    private fun loadCurrencies() {
        viewModelScope.launch {
            val result = currencyRepository.getAllActive()
            if (result is AppResult.Success) {
                state = state.copy(currencies = result.data.associateBy { it.code })
            }
        }
    }

    private fun loadWallets() {
        viewModelScope.launch {
            val result = walletRepository.getMyAll()
            if (result is AppResult.Success) {
                state = state.copy(wallets = result.data)
            }
        }
    }

    private fun loadCards() {
        viewModelScope.launch {
            val result = cardRepository.getMyAll()
            if (result is AppResult.Success) {
                state = state.copy(cards = result.data)
            }
        }
    }

    fun loadTransactions() {
        if (state.isLoading) return

        viewModelScope.launch {
            state = state.copy(isLoading = true, currentPage = 0, isEndReached = false)
            fetchPage(initialWalletId, 0)
        }
    }

    fun loadNextPage() {
        if (state.isMoreLoading || state.isEndReached) return

        viewModelScope.launch {
            state = state.copy(isMoreLoading = true)
            fetchPage(state.currentWalletId, state.currentPage + 1)
        }
    }

    fun toggleFilterExpanded() {
        state = state.copy(isFilterExpanded = !state.isFilterExpanded)
    }

    fun setSelectedWallet(walletId: Long?) {
        viewModelScope.launch {
            state = state.copy(
                isLoading = true, currentPage = 0, isEndReached = false,
                transactions = emptyList(), currentWalletId = walletId
            )
            fetchPage(walletId, 0)
        }
    }

    fun setFilterCard(cardId: Long?) {
        val label = cardId?.let { id -> state.cards.find { it.id == id }?.cardSuffix }
        state = state.copy(filterCardId = cardId, filterLabel = label)
        reloadWithCurrentWallet()
    }

    fun setFilterDirection(direction: TransactionDirection) {
        state = state.copy(filterDirection = direction)
        reloadWithCurrentWallet()
    }

    fun setFilterType(type: TransactionType?) {
        state = state.copy(filterType = type)
        reloadWithCurrentWallet()
    }

    fun setFilterDateFrom(date: LocalDate?) {
        state = state.copy(filterDateFrom = date)
        reloadWithCurrentWallet()
    }

    fun setFilterDateTo(date: LocalDate?) {
        state = state.copy(filterDateTo = date)
        reloadWithCurrentWallet()
    }

    fun setSortOrder(order: TransactionSortOrder) {
        state = state.copy(sortOrder = order)
        reloadWithCurrentWallet()
    }

    fun clearFilters() {
        viewModelScope.launch {
            state = state.copy(
                currentWalletId = null,
                filterDirection = TransactionDirection.ALL,
                filterType = null,
                filterLabel = null,
                filterCardId = null,
                filterDateFrom = null,
                filterDateTo = null,
                sortOrder = TransactionSortOrder.NEWEST,
                isLoading = true,
                currentPage = 0,
                isEndReached = false,
                transactions = emptyList()
            )
            fetchPage(null, 0)
        }
    }

    private fun reloadWithCurrentWallet() {
        viewModelScope.launch {
            state = state.copy(isLoading = true, currentPage = 0, isEndReached = false, transactions = emptyList())
            fetchPage(state.currentWalletId, 0)
        }
    }

    private suspend fun fetchPage(walletId: Long?, page: Int) {
        val pageSize = 15
        val sort = state.sortOrder.apiValue
        val type = state.filterType
        val label = state.filterLabel
        val createdFrom = state.filterDateFrom?.atStartOfDay()
        val createdTo = state.filterDateTo?.atTime(23, 59, 59)

        if (walletId == null) {
            val result = transactionRepository.search(
                filter = TransactionFilterInput(
                    label = label,
                    type = type,
                    createdFrom = createdFrom,
                    createdTo = createdTo
                ),
                page = page, size = pageSize, sort = sort
            )
            handleSingleResult(result, null, page, pageSize)
            return
        }

        when (state.filterDirection) {
            TransactionDirection.SENT -> {
                val result = transactionRepository.search(
                    filter = TransactionFilterInput(
                        label = label,
                        senderWalletId = walletId,
                        type = type,
                        createdFrom = createdFrom,
                        createdTo = createdTo
                    ),
                    page = page, size = pageSize, sort = sort
                )
                handleSingleResult(result, walletId, page, pageSize)
            }
            TransactionDirection.RECEIVED -> {
                val result = transactionRepository.search(
                    filter = TransactionFilterInput(
                        label = label,
                        recipientWalletId = walletId,
                        type = type,
                        createdFrom = createdFrom,
                        createdTo = createdTo
                    ),
                    page = page, size = pageSize, sort = sort
                )
                handleSingleResult(result, walletId, page, pageSize)
            }
            TransactionDirection.ALL -> {
                val sentDeferred = viewModelScope.async {
                    transactionRepository.search(
                        filter = TransactionFilterInput(
                            label = label,
                            senderWalletId = walletId,
                            type = type,
                            createdFrom = createdFrom,
                            createdTo = createdTo
                        ),
                        page = page, size = pageSize, sort = sort
                    )
                }
                val receivedDeferred = viewModelScope.async {
                    transactionRepository.search(
                        filter = TransactionFilterInput(
                            label = label,
                            recipientWalletId = walletId,
                            type = type,
                            createdFrom = createdFrom,
                            createdTo = createdTo
                        ),
                        page = page, size = pageSize, sort = sort
                    )
                }

                val sentResult = sentDeferred.await()
                val receivedResult = receivedDeferred.await()

                if (sentResult is AppResult.Success && receivedResult is AppResult.Success) {
                    val merged = (sentResult.data + receivedResult.data)
                        .distinctBy { it.id }
                        .let { list ->
                            when (state.sortOrder) {
                                TransactionSortOrder.NEWEST  -> list.sortedByDescending { it.createdAt }
                                TransactionSortOrder.OLDEST  -> list.sortedBy { it.createdAt }
                                TransactionSortOrder.HIGHEST -> list.sortedByDescending { it.senderAmount }
                                TransactionSortOrder.LOWEST  -> list.sortedBy { it.senderAmount }
                            }
                        }

                    val isEnd = sentResult.data.size < pageSize && receivedResult.data.size < pageSize

                    state = state.copy(
                        transactions = if (page == 0) merged else state.transactions + merged,
                        isLoading = false,
                        isMoreLoading = false,
                        currentPage = page,
                        isEndReached = isEnd,
                        currentWalletId = walletId
                    )
                } else {
                    state = state.copy(isLoading = false, isMoreLoading = false)
                    notificationManager.showError(context.getString(R.string.msg_failed_load_transactions))
                }
            }
        }
    }

    private fun handleSingleResult(
        result: AppResult<List<com.krushkov.virtualwallet.domain.models.outputs.transaction.Transaction>>,
        walletId: Long?,
        page: Int,
        pageSize: Int
    ) {
        if (result is AppResult.Success) {
            state = state.copy(
                transactions = if (page == 0) result.data else state.transactions + result.data,
                isLoading = false,
                isMoreLoading = false,
                currentPage = page,
                isEndReached = result.data.size < pageSize,
                currentWalletId = walletId
            )
        } else {
            state = state.copy(isLoading = false, isMoreLoading = false)
            viewModelScope.launch { notificationManager.showError(context.getString(R.string.msg_failed_load_transactions)) }
        }
    }
}
