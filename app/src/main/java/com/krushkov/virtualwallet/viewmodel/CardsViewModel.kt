package com.krushkov.virtualwallet.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krushkov.virtualwallet.R
import com.krushkov.virtualwallet.domain.error.getMessage
import com.krushkov.virtualwallet.domain.models.inputs.transaction.TransactionFilterInput
import com.krushkov.virtualwallet.domain.models.outputs.card.Card
import com.krushkov.virtualwallet.domain.models.outputs.card.CardStatus
import com.krushkov.virtualwallet.domain.models.outputs.transaction.Transaction
import com.krushkov.virtualwallet.domain.repositories.CardRepository
import com.krushkov.virtualwallet.domain.repositories.TransactionRepository
import com.krushkov.virtualwallet.domain.result.AppResult
import com.krushkov.virtualwallet.domain.result.fold
import com.krushkov.virtualwallet.viewmodel.states.CardsState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val cardRepository: CardRepository,
    private val transactionRepository: TransactionRepository,
    private val walletRepository: com.krushkov.virtualwallet.domain.repositories.WalletRepository,
    private val topUpRepository: com.krushkov.virtualwallet.domain.repositories.TopUpRepository,
    private val currencyRepository: com.krushkov.virtualwallet.domain.repositories.CurrencyRepository,
    private val notificationManager: com.krushkov.virtualwallet.ui.utils.NotificationManager
) : ViewModel() {

    var state by mutableStateOf(CardsState())
        private set

    init {
        loadCards()
        loadCurrencies()
    }

    private fun loadCurrencies() {
        viewModelScope.launch {
            val result = currencyRepository.getAllActive()
            if (result is AppResult.Success) {
                state = state.copy(currencies = result.data.associateBy { it.code })
            }
        }
    }

    fun loadCards() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)

            cardRepository.getMyAll().fold(
                onSuccess = { cards ->
                    val preserved = cards.find { it.id == state.selectedCard?.id } ?: cards.firstOrNull()
                    state = state.copy(
                        isLoading = false,
                        cards = cards,
                        selectedCard = preserved
                    )
                    preserved?.let { loadTopUps(listOf(it.cardSuffix)) }
                },
                onError = {
                    state = state.copy(isLoading = false)
                    viewModelScope.launch {
                        notificationManager.showError(it.getMessage())
                    }
                }
            )
        }
    }

    fun selectCard(card: Card) {
        if (state.selectedCard?.id == card.id) return
        state = state.copy(
            selectedCard = card,
            isDeactivateDialogVisible = false,
            isActivateDialogVisible = false,
            isRemoveConfirmVisible = false
        )
        loadTopUps(listOf(card.cardSuffix))
        viewModelScope.launch {
            when (val result = cardRepository.getById(card.id)) {
                is AppResult.Success -> state = state.copy(
                    selectedCard = result.data,
                    cards = state.cards.map { if (it.id == result.data.id) result.data else it }
                )
                is AppResult.Error -> {}
            }
        }
    }

    fun onCardStatusActionClick() {
        when (state.selectedCard?.status) {
            CardStatus.ACTIVE -> {
                if (state.isDeactivateDialogVisible) {
                    confirmDeactivate()
                } else {
                    state = state.copy(
                        isDeactivateDialogVisible = true,
                        isActivateDialogVisible = false,
                        isRemoveConfirmVisible = false
                    )
                    showClickAgainWarning()
                }
            }
            CardStatus.USER_DEACTIVATED -> {
                if (state.isActivateDialogVisible) {
                    confirmActivate()
                } else {
                    state = state.copy(
                        isActivateDialogVisible = true,
                        isDeactivateDialogVisible = false,
                        isRemoveConfirmVisible = false
                    )
                    showClickAgainWarning()
                }
            }
            else -> {}
        }
    }

    fun dismissCardStatusDialog() {
        state = state.copy(isDeactivateDialogVisible = false, isActivateDialogVisible = false)
    }

    fun confirmDeactivate() {
        val cardId = state.selectedCard?.id ?: return
        viewModelScope.launch {
            state = state.copy(isDeactivateDialogVisible = false, isCardActionLoading = true)
            when (val result = cardRepository.deactivate(cardId)) {
                is AppResult.Success -> {
                    notificationManager.showSuccess(context.getString(R.string.msg_card_deactivated))
                    loadCards()
                }
                is AppResult.Error -> notificationManager.showError(result.error.getMessage())
            }
            state = state.copy(isCardActionLoading = false)
        }
    }

    fun confirmActivate() {
        val cardId = state.selectedCard?.id ?: return
        viewModelScope.launch {
            state = state.copy(isActivateDialogVisible = false, isCardActionLoading = true)
            when (val result = cardRepository.activate(cardId)) {
                is AppResult.Success -> {
                    notificationManager.showSuccess(context.getString(R.string.msg_card_activated))
                    loadCards()
                }
                is AppResult.Error -> notificationManager.showError(result.error.getMessage())
            }
            state = state.copy(isCardActionLoading = false)
        }
    }

    fun showRemoveConfirm() {
        if (state.isRemoveConfirmVisible) {
            confirmRemove()
        } else {
            state = state.copy(
                isRemoveConfirmVisible = true,
                isDeactivateDialogVisible = false,
                isActivateDialogVisible = false
            )
            showClickAgainWarning()
        }
    }

    fun confirmRemove() {
        val cardId = state.selectedCard?.id ?: return
        viewModelScope.launch {
            state = state.copy(isRemoveConfirmVisible = false, isRemoveLoading = true)
            when (val result = cardRepository.remove(cardId)) {
                is AppResult.Success -> {
                    notificationManager.showSuccess(context.getString(R.string.msg_card_removed))
                    state = state.copy(selectedCard = null)
                    loadCards()
                }
                is AppResult.Error -> notificationManager.showError(result.error.getMessage())
            }
            state = state.copy(isRemoveLoading = false)
        }
    }

    private fun showClickAgainWarning() {
        viewModelScope.launch {
            notificationManager.showWarning(context.getString(R.string.msg_click_again_confirm))
        }
    }

    private fun loadTopUps(suffixes: List<String>) {
        viewModelScope.launch {
            state = state.copy(isTopUpsLoading = true)
            
            val allTopUps = mutableListOf<Transaction>()
            
            suffixes.forEach { suffix ->
                val result = transactionRepository.search(
                    filter = TransactionFilterInput(
                        label = suffix,
                        type = com.krushkov.virtualwallet.domain.models.outputs.transaction.TransactionType.TOP_UP
                    ),
                    page = 0,
                    size = 7,
                    sort = "createdAt,desc"
                )
                if (result is AppResult.Success) {
                    allTopUps.addAll(result.data)
                }
            }
            
            state = state.copy(
                isTopUpsLoading = false,
                topUps = allTopUps.sortedByDescending { it.createdAt }.take(7)
            )
        }
    }
}
