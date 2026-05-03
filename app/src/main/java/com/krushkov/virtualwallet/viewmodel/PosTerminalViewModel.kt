package com.krushkov.virtualwallet.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krushkov.virtualwallet.domain.repositories.CurrencyRepository
import com.krushkov.virtualwallet.domain.result.AppResult
import com.krushkov.virtualwallet.nfc.PaymentEventBus
import com.krushkov.virtualwallet.nfc.PendingPayment
import com.krushkov.virtualwallet.viewmodel.states.NfcStatus
import com.krushkov.virtualwallet.viewmodel.states.PosCurrencyOption
import com.krushkov.virtualwallet.viewmodel.states.PosTerminalState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PosTerminalViewModel @Inject constructor(
    private val currencyRepository: CurrencyRepository
) : ViewModel() {

    var state by mutableStateOf(PosTerminalState())
        private set

    init {
        loadCurrencies()
    }

    fun onAmountChange(value: String) {
        state = state.copy(amount = value, nfcStatus = NfcStatus.IDLE)
    }

    fun onCurrencyChange(value: String) {
        state = state.copy(currencyCode = value, nfcStatus = NfcStatus.IDLE)
    }

    fun toggleCurrencyMenu(expanded: Boolean) {
        state = state.copy(isCurrencyMenuExpanded = expanded)
    }

    fun setNfcStatus(status: NfcStatus) {
        state = state.copy(nfcStatus = status)
    }

    fun setNfcAvailable(available: Boolean) {
        state = state.copy(isNfcAvailable = available)
    }

    fun canStartPaymentRequest(): Boolean {
        return state.amount.toBigDecimalOrNull() != null &&
                state.currencyCode.isNotBlank() &&
                state.currencyOptions.any { it.code == state.currencyCode }
    }

    fun startPaymentRequest() {
        val amount = state.amount.toBigDecimalOrNull() ?: return
        PaymentEventBus.post(
            PendingPayment(
                amount = amount,
                currencyCode = state.currencyCode.trim().uppercase(),
                merchantReference = ""
            )
        )
        state = state.copy(nfcStatus = NfcStatus.SENT)
    }

    fun buildPayload(): String? {
        val amount = state.amount.toBigDecimalOrNull() ?: return null
        return "$amount|${state.currencyCode.trim().uppercase()}"
    }

    private fun loadCurrencies() {
        viewModelScope.launch {
            val result = currencyRepository.getAllActive()
            if (result !is AppResult.Success) return@launch

            val options = result.data
                .mapNotNull { currency ->
                    currency.code.takeIf { it.isNotBlank() }?.let {
                        PosCurrencyOption(
                            code = it.uppercase(),
                            name = currency.name
                        )
                    }
                }
                .distinctBy { it.code }
                .sortedBy { it.code }

            state = state.copy(
                currencyOptions = options,
                currencyCode = options.firstOrNull()?.code.orEmpty()
            )
        }
    }
}
