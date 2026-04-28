package com.krushkov.virtualwallet.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krushkov.virtualwallet.domain.models.inputs.wallet.WalletCreateInput
import com.krushkov.virtualwallet.domain.models.outputs.currency.Currency
import com.krushkov.virtualwallet.domain.repositories.CurrencyRepository
import com.krushkov.virtualwallet.domain.repositories.WalletRepository
import com.krushkov.virtualwallet.domain.result.AppResult
import com.krushkov.virtualwallet.viewmodel.states.CreateWalletState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateWalletViewModel @Inject constructor(
    private val walletRepository: WalletRepository,
    private val currencyRepository: CurrencyRepository,
    private val notificationManager: com.krushkov.virtualwallet.ui.utils.NotificationManager
) : ViewModel() {

    var state by mutableStateOf(CreateWalletState())
        private set

    init {
        loadCurrencies()
    }

    private fun loadCurrencies() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            when (val result = currencyRepository.getAllActive()) {
                is AppResult.Success -> {
                    state = state.copy(
                        currencies = result.data,
                        selectedCurrency = result.data.firstOrNull(),
                        isLoading = false
                    )
                }
                is AppResult.Error -> {
                    state = state.copy(
                        isLoading = false
                    )
                    viewModelScope.launch {
                        notificationManager.showError("Failed to load currencies")
                    }
                }
            }
        }
    }

    fun onNameChange(name: String) {
        state = state.copy(name = name)
    }

    fun onCurrencySelect(currency: Currency) {
        state = state.copy(selectedCurrency = currency)
    }

    fun onMakeDefaultChange(makeDefault: Boolean) {
        state = state.copy(makeDefault = makeDefault)
    }

    fun toggleCurrencyMenu(expanded: Boolean) {
        state = state.copy(isCurrencyMenuExpanded = expanded)
    }

    fun createWallet() {
        val selectedCurrency = state.selectedCurrency ?: return
        if (state.name.isBlank()) {
            viewModelScope.launch {
                notificationManager.showError("Wallet name cannot be empty")
            }
            return
        }

        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val input = WalletCreateInput(
                name = state.name,
                currencyCode = selectedCurrency.code
            )

            when (val result = walletRepository.create(input)) {
                is AppResult.Success -> {
                    if (state.makeDefault) {
                        walletRepository.setDefault(result.data.id)
                    }
                    notificationManager.showSuccess("Wallet created successfully")
                    state = state.copy(isLoading = false, isSuccess = true)
                }
                is AppResult.Error -> {
                    state = state.copy(
                        isLoading = false
                    )
                    notificationManager.showError("Failed to create wallet")
                }
            }
        }
    }
    
    fun resetSuccess() {
        state = state.copy(isSuccess = false)
    }
}