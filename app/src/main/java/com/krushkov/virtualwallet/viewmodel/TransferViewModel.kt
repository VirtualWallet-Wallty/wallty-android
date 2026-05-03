package com.krushkov.virtualwallet.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krushkov.virtualwallet.R
import com.krushkov.virtualwallet.domain.error.getMessage
import com.krushkov.virtualwallet.domain.models.inputs.TransferInput
import com.krushkov.virtualwallet.domain.models.outputs.wallet.Wallet
import com.krushkov.virtualwallet.domain.repositories.CurrencyRepository
import com.krushkov.virtualwallet.domain.repositories.TransferRepository
import com.krushkov.virtualwallet.domain.repositories.UserRepository
import com.krushkov.virtualwallet.domain.repositories.WalletRepository
import com.krushkov.virtualwallet.domain.result.AppResult
import com.krushkov.virtualwallet.ui.utils.NotificationManager
import com.krushkov.virtualwallet.viewmodel.states.TransferState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransferViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val walletRepository: WalletRepository,
    private val transferRepository: TransferRepository,
    private val userRepository: UserRepository,
    private val currencyRepository: CurrencyRepository,
    private val notificationManager: NotificationManager
) : ViewModel() {

    var state by mutableStateOf(TransferState())
        private set

    init {
        loadWallets()
    }

    fun loadWallets() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            when (val result = walletRepository.getMyAll()) {
                is AppResult.Success -> {
                    val wallets = result.data
                    val default = wallets.firstOrNull { it.isDefault } ?: wallets.firstOrNull()
                    state = state.copy(wallets = wallets, selectedWallet = default, isLoading = false)
                    default?.let { resolveSymbol(it) }
                }
                is AppResult.Error -> state = state.copy(isLoading = false)
            }
        }
    }

    fun selectWallet(wallet: Wallet) {
        state = state.copy(selectedWallet = wallet, isWalletDropdownExpanded = false)
        viewModelScope.launch { resolveSymbol(wallet) }
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

    fun toggleWalletDropdown(expanded: Boolean) {
        state = state.copy(isWalletDropdownExpanded = expanded)
    }

    fun onQrScanned(rawValue: String) {
        if (state.navigateToConfirm || state.isLoadingRecipient) return
        val parts = rawValue.split("|")
        if (parts.size == 2 && parts[0] == "WALLTY_TRANSFER") {
            val recipientId = parts[1].toLongOrNull() ?: return
            state = state.copy(
                scannedRecipientId = recipientId,
                isLoadingRecipient = true,
                sendAmount = ""
            )
            viewModelScope.launch {
                try {
                    when (val result = userRepository.getById(recipientId)) {
                        is AppResult.Success -> state = state.copy(
                            recipientProfile = result.data,
                            isLoadingRecipient = false,
                            navigateToConfirm = true
                        )
                        is AppResult.Error -> {
                            notificationManager.showError(result.error.getMessage())
                            state = state.copy(
                                isLoadingRecipient = false,
                                scannedRecipientId = null
                            )
                        }
                    }
                } catch (e: Exception) {
                    notificationManager.showError(context.getString(R.string.msg_failed_load_recipient))
                    state = state.copy(
                        isLoadingRecipient = false,
                        scannedRecipientId = null
                    )
                }
            }
        }
    }

    fun onNavigateToConfirmHandled() {
        state = state.copy(navigateToConfirm = false)
    }

    fun resetScanState() {
        state = state.copy(
            scannedRecipientId = null,
            recipientProfile = null,
            sendAmount = "",
            navigateToConfirm = false
        )
    }

    fun onSendAmountChange(value: String) {
        state = state.copy(sendAmount = value)
    }

    fun sendTransfer() {
        val amount = state.sendAmount.toBigDecimalOrNull() ?: return
        val recipientId = state.scannedRecipientId ?: return
        val sourceWallet = state.selectedWallet ?: return
        val currencyCode = sourceWallet.currencyCode ?: sourceWallet.currency?.code ?: return

        viewModelScope.launch {
            state = state.copy(isSendLoading = true)
            try {
                when (val result = transferRepository.transfer(
                    TransferInput(
                        amount = amount,
                        currencyCode = currencyCode,
                        recipientId = recipientId,
                        sourceWalletId = sourceWallet.id
                    )
                )) {
                    is AppResult.Success -> {
                        notificationManager.showSuccess(context.getString(R.string.msg_transfer_sent))
                        state = state.copy(
                            isSendLoading = false,
                            isTransferSuccess = true,
                            scannedRecipientId = null,
                            recipientProfile = null,
                            sendAmount = ""
                        )
                    }
                    is AppResult.Error -> {
                        notificationManager.showError(result.error.getMessage())
                        state = state.copy(isSendLoading = false)
                    }
                }
            } catch (e: Exception) {
                notificationManager.showError(context.getString(R.string.msg_something_went_wrong))
                state = state.copy(isSendLoading = false)
            }
        }
    }

    fun resetTransferSuccess() {
        state = state.copy(isTransferSuccess = false)
    }
}
