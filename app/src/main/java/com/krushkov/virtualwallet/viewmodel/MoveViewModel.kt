package com.krushkov.virtualwallet.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krushkov.virtualwallet.domain.error.getMessage
import com.krushkov.virtualwallet.domain.models.inputs.TransferInput
import com.krushkov.virtualwallet.domain.models.outputs.wallet.Wallet
import com.krushkov.virtualwallet.domain.repositories.AuthRepository
import com.krushkov.virtualwallet.domain.repositories.CurrencyRepository
import com.krushkov.virtualwallet.domain.repositories.TransferRepository
import com.krushkov.virtualwallet.domain.repositories.WalletRepository
import com.krushkov.virtualwallet.domain.result.AppResult
import com.krushkov.virtualwallet.ui.utils.NotificationManager
import com.krushkov.virtualwallet.viewmodel.states.MoveState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoveViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val walletRepository: WalletRepository,
    private val transferRepository: TransferRepository,
    private val authRepository: AuthRepository,
    private val currencyRepository: CurrencyRepository,
    private val notificationManager: NotificationManager
) : ViewModel() {

    private val sourceWalletId: Long = savedStateHandle["walletId"] ?: -1L

    var state by mutableStateOf(MoveState())
        private set

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            try {
                val walletResult = walletRepository.getById(sourceWalletId)
                val allWalletsResult = walletRepository.getMyAll()

                if (walletResult is AppResult.Success) {
                    state = state.copy(fromWallet = walletResult.data)
                    resolveSymbol(walletResult.data)
                }

                if (allWalletsResult is AppResult.Success) {
                    val others = allWalletsResult.data.filter { it.id != sourceWalletId }
                    state = state.copy(
                        wallets = others,
                        selectedToWallet = others.firstOrNull { it.isDefault } ?: others.firstOrNull()
                    )
                }
            } catch (e: Exception) {
                notificationManager.showError("Failed to load wallets")
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

    fun selectToWallet(wallet: Wallet) {
        state = state.copy(selectedToWallet = wallet, isDropdownExpanded = false)
    }

    fun toggleDropdown(expanded: Boolean) {
        state = state.copy(isDropdownExpanded = expanded)
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
                val fromWallet = state.fromWallet ?: run {
                    state = state.copy(isSubmitLoading = false)
                    return@launch
                }
                val currencyCode = fromWallet.currencyCode ?: fromWallet.currency?.code ?: run {
                    state = state.copy(isSubmitLoading = false)
                    return@launch
                }
                val toWallet = state.selectedToWallet ?: run {
                    state = state.copy(isSubmitLoading = false)
                    return@launch
                }
                val userId = (authRepository.getMe() as? AppResult.Success)?.data?.id ?: run {
                    state = state.copy(isSubmitLoading = false)
                    return@launch
                }

                when (val result = transferRepository.transfer(
                    TransferInput(
                        amount = amount,
                        currencyCode = currencyCode,
                        recipientId = userId,
                        sourceWalletId = fromWallet.id,
                        targetWalletId = toWallet.id
                    )
                )) {
                    is AppResult.Success -> {
                        notificationManager.showSuccess(result.message ?: "Funds moved successfully!")
                        state = state.copy(isSubmitLoading = false, isSuccess = true)
                    }
                    is AppResult.Error -> {
                        notificationManager.showError(result.error.getMessage())
                        state = state.copy(isSubmitLoading = false)
                    }
                }
            } catch (e: Exception) {
                notificationManager.showError("Something went wrong")
                state = state.copy(isSubmitLoading = false)
            }
        }
    }

    fun resetSuccess() {
        state = state.copy(isSuccess = false)
    }
}
