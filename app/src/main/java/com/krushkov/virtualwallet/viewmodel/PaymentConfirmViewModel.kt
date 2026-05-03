package com.krushkov.virtualwallet.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krushkov.virtualwallet.R
import com.krushkov.virtualwallet.domain.error.getMessage
import com.krushkov.virtualwallet.domain.models.inputs.PaymentInput
import com.krushkov.virtualwallet.domain.repositories.PaymentRepository
import com.krushkov.virtualwallet.domain.repositories.WalletRepository
import com.krushkov.virtualwallet.domain.result.AppResult
import com.krushkov.virtualwallet.nfc.PaymentEventBus
import com.krushkov.virtualwallet.ui.utils.NotificationManager
import com.krushkov.virtualwallet.viewmodel.states.PaymentConfirmState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentConfirmViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val paymentRepository: PaymentRepository,
    private val walletRepository: WalletRepository,
    private val notificationManager: NotificationManager
) : ViewModel() {

    var state by mutableStateOf(PaymentConfirmState())
        private set

    init {
        state = state.copy(pendingPayment = PaymentEventBus.pendingPayment.value)
        loadWallets()
    }

    private fun loadWallets() {
        viewModelScope.launch {
            when (val result = walletRepository.getMyAll()) {
                is AppResult.Success -> {
                    val wallets = result.data
                    val default = wallets.firstOrNull { it.isDefault } ?: wallets.firstOrNull()
                    state = state.copy(wallets = wallets, wallet = default)
                }
                is AppResult.Error -> {}
            }
        }
    }

    fun confirm() {
        val payment = state.pendingPayment ?: return
        val wallet = state.wallet ?: return

        viewModelScope.launch {
            state = state.copy(isLoading = true)
            when (val result = paymentRepository.pay(
                PaymentInput(
                    amount = payment.amount,
                    currencyCode = payment.currencyCode,
                    merchantReference = payment.merchantReference,
                    sourceWalletId = wallet.id
                )
            )) {
                is AppResult.Success -> {
                    PaymentEventBus.consume()
                    notificationManager.showSuccess(context.getString(R.string.msg_payment_successful))
                    state = state.copy(isLoading = false, isDone = true)
                }
                is AppResult.Error -> {
                    notificationManager.showError(result.error.getMessage())
                    state = state.copy(isLoading = false)
                }
            }
        }
    }

    fun cancel() {
        PaymentEventBus.consume()
        state = state.copy(isDone = true)
    }
}
