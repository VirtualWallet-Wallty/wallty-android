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
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoveViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
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
                    val wallet = walletResult.data
                    val code = wallet.currencyCode ?: wallet.currency?.code
                    state = state.copy(fromWallet = wallet, selectedCurrencyCode = code)
                    code?.let { resolveSymbolForCode(it) }
                }

                if (allWalletsResult is AppResult.Success) {
                    val all = allWalletsResult.data
                    val defaultTo = all.firstOrNull { it.id != sourceWalletId && it.isDefault }
                        ?: all.firstOrNull { it.id != sourceWalletId }
                    state = state.copy(wallets = all, selectedToWallet = defaultTo)
                }
            } catch (e: Exception) {
                notificationManager.showError(context.getString(R.string.msg_failed_load_wallets))
            }
            state = state.copy(isLoading = false)
        }
    }

    private suspend fun resolveSymbolForCode(code: String) {
        val walletWithCode = state.wallets.firstOrNull {
            it.currencyCode == code || it.currency?.code == code
        }
        val directSymbol = walletWithCode?.currency?.symbol
        if (directSymbol != null) {
            state = state.copy(currencySymbol = directSymbol)
            return
        }
        when (val result = currencyRepository.getByCode(code)) {
            is AppResult.Success -> state = state.copy(currencySymbol = result.data.symbol)
            is AppResult.Error -> {}
        }
    }

    fun selectCurrency(code: String) {
        state = state.copy(selectedCurrencyCode = code)
        viewModelScope.launch { resolveSymbolForCode(code) }
    }

    fun selectFromWallet(wallet: Wallet) {
        val newCode = wallet.currencyCode ?: wallet.currency?.code
        if (state.selectedToWallet?.id == wallet.id) {
            val newFrom = state.selectedToWallet!!
            val newTo = state.fromWallet
            val fromCode = newFrom.currencyCode ?: newFrom.currency?.code
            state = state.copy(
                fromWallet = newFrom,
                selectedToWallet = newTo,
                selectedCurrencyCode = fromCode,
                isFromDropdownExpanded = false
            )
            fromCode?.let { viewModelScope.launch { resolveSymbolForCode(it) } }
        } else {
            state = state.copy(
                fromWallet = wallet,
                selectedCurrencyCode = newCode,
                isFromDropdownExpanded = false
            )
            newCode?.let { viewModelScope.launch { resolveSymbolForCode(it) } }
        }
    }

    fun toggleFromDropdown(expanded: Boolean) {
        state = state.copy(isFromDropdownExpanded = expanded)
    }

    fun switchWallets() {
        val from = state.fromWallet ?: return
        val to = state.selectedToWallet ?: return
        val newFromCode = to.currencyCode ?: to.currency?.code
        state = state.copy(
            fromWallet = to,
            selectedToWallet = from,
            selectedCurrencyCode = newFromCode
        )
        newFromCode?.let { viewModelScope.launch { resolveSymbolForCode(it) } }
    }

    fun selectToWallet(wallet: Wallet) {
        if (state.fromWallet?.id == wallet.id) {
            val newFrom = state.selectedToWallet ?: return
            val newTo = state.fromWallet
            val fromCode = newFrom.currencyCode ?: newFrom.currency?.code
            state = state.copy(
                fromWallet = newFrom,
                selectedToWallet = newTo,
                selectedCurrencyCode = fromCode,
                isDropdownExpanded = false
            )
            fromCode?.let { viewModelScope.launch { resolveSymbolForCode(it) } }
        } else {
            state = state.copy(selectedToWallet = wallet, isDropdownExpanded = false)
        }
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
                val currencyCode = state.selectedCurrencyCode
                    ?: fromWallet.currencyCode
                    ?: fromWallet.currency?.code
                    ?: run {
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
                        notificationManager.showSuccess(result.message ?: context.getString(R.string.msg_funds_moved))
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
