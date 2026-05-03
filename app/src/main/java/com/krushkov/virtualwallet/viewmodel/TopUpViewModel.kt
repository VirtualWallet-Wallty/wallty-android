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
import com.krushkov.virtualwallet.domain.models.inputs.TopUpInput
import com.krushkov.virtualwallet.domain.models.outputs.card.Card
import com.krushkov.virtualwallet.domain.models.outputs.wallet.Wallet
import com.krushkov.virtualwallet.domain.repositories.CardRepository
import com.krushkov.virtualwallet.domain.repositories.CurrencyRepository
import com.krushkov.virtualwallet.domain.repositories.TopUpRepository
import com.krushkov.virtualwallet.domain.repositories.WalletRepository
import com.krushkov.virtualwallet.domain.result.AppResult
import com.krushkov.virtualwallet.ui.utils.NotificationManager
import com.krushkov.virtualwallet.viewmodel.states.TopUpState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class TopUpViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle,
    private val walletRepository: WalletRepository,
    private val cardRepository: CardRepository,
    private val currencyRepository: CurrencyRepository,
    private val topUpRepository: TopUpRepository,
    private val notificationManager: NotificationManager
) : ViewModel() {

    private val mode: String = savedStateHandle["mode"] ?: "wallet"
    private val sourceId: Long = savedStateHandle["id"] ?: -1L

    var state by mutableStateOf(TopUpState(isWalletMode = mode == "wallet"))
        private set

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            if (state.isWalletMode) {
                val walletResult = walletRepository.getById(sourceId)
                val cardsResult = cardRepository.getMyAll()
                if (walletResult is AppResult.Success) {
                    state = state.copy(wallet = walletResult.data)
                    resolveSymbol(walletResult.data)
                }
                if (cardsResult is AppResult.Success) {
                    state = state.copy(
                        cards = cardsResult.data,
                        selectedCard = cardsResult.data.firstOrNull()
                    )
                }
            } else {
                val cardResult = cardRepository.getById(sourceId)
                val walletsResult = walletRepository.getMyAll()
                if (cardResult is AppResult.Success) {
                    state = state.copy(card = cardResult.data)
                }
                if (walletsResult is AppResult.Success) {
                    val wallets = walletsResult.data
                    val default = wallets.firstOrNull { it.isDefault } ?: wallets.firstOrNull()
                    state = state.copy(wallets = wallets, selectedWallet = default)
                    default?.let { resolveSymbol(it) }
                }
            }
            state = state.copy(isLoading = false)
        }
    }

    private suspend fun resolveSymbol(wallet: Wallet) {
        val direct = wallet.currency?.symbol
        if (direct != null) {
            state = state.copy(currencySymbol = direct)
            return
        }
        val code = wallet.currencyCode ?: return
        when (val result = currencyRepository.getByCode(code)) {
            is AppResult.Success -> state = state.copy(currencySymbol = result.data.symbol)
            is AppResult.Error -> {}
        }
    }

    fun selectCard(card: Card) {
        state = state.copy(selectedCard = card, isCardDropdownExpanded = false)
    }

    fun toggleCardDropdown(expanded: Boolean) {
        state = state.copy(isCardDropdownExpanded = expanded)
    }

    fun selectWallet(wallet: Wallet) {
        state = state.copy(selectedWallet = wallet, isWalletDropdownExpanded = false)
        viewModelScope.launch { resolveSymbol(wallet) }
    }

    fun toggleWalletDropdown(expanded: Boolean) {
        state = state.copy(isWalletDropdownExpanded = expanded)
    }

    fun onAmountChange(value: String) {
        state = state.copy(amount = value)
    }

    fun confirm() {
        viewModelScope.launch {
            state = state.copy(isSubmitLoading = true)
            try {
                val amount = state.amount.toBigDecimalOrNull() ?: run {
                    state = state.copy(isSubmitLoading = false)
                    return@launch
                }
                val input = if (state.isWalletMode) {
                    val wallet = state.wallet ?: run { state = state.copy(isSubmitLoading = false); return@launch }
                    val card = state.selectedCard ?: run { state = state.copy(isSubmitLoading = false); return@launch }
                    val code = wallet.currencyCode ?: wallet.currency?.code ?: run { state = state.copy(isSubmitLoading = false); return@launch }
                    TopUpInput(walletId = wallet.id, amount = amount, currencyCode = code, cardId = card.id, externalReference = UUID.randomUUID().toString())
                } else {
                    val wallet = state.selectedWallet ?: run { state = state.copy(isSubmitLoading = false); return@launch }
                    val card = state.card ?: run { state = state.copy(isSubmitLoading = false); return@launch }
                    val code = wallet.currencyCode ?: wallet.currency?.code ?: run { state = state.copy(isSubmitLoading = false); return@launch }
                    TopUpInput(walletId = wallet.id, amount = amount, currencyCode = code, cardId = card.id, externalReference = UUID.randomUUID().toString())
                }
                when (val result = topUpRepository.topUp(input)) {
                    is AppResult.Success -> {
                        notificationManager.showSuccess(result.message ?: context.getString(R.string.msg_topup_successful))
                        state = state.copy(isSubmitLoading = false, isSuccess = true)
                    }
                    is AppResult.Error -> {
                        notificationManager.showError(result.error.getMessage())
                        state = state.copy(isSubmitLoading = false)
                    }
                }
            } catch (e: Exception) {
                notificationManager.showError(context.getString(R.string.msg_something_went_wrong))
                state = state.copy(isSubmitLoading = false)
            }
        }
    }

    fun resetSuccess() {
        state = state.copy(isSuccess = false)
    }
}
