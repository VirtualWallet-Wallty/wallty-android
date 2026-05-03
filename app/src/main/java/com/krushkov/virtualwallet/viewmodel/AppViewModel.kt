package com.krushkov.virtualwallet.viewmodel

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krushkov.virtualwallet.nfc.PaymentEventBus
import com.krushkov.virtualwallet.nfc.PaymentHceService
import com.krushkov.virtualwallet.domain.repositories.WalletRepository
import com.krushkov.virtualwallet.domain.result.AppResult
import com.krushkov.virtualwallet.ui.utils.NotificationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val walletRepository: WalletRepository,
    val notificationManager: NotificationManager
) : ViewModel() {

    var hasWallets by mutableStateOf(false)
        private set

    var isPayModeActive by mutableStateOf(false)
        private set

    var isHomeNormalMode by mutableStateOf(false)
        private set

    var navigateToPaymentConfirm by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch { observePaymentEvents() }
    }

    private suspend fun observePaymentEvents() {
        PaymentEventBus.pendingPayment.collect { payment ->
            if (payment != null && !navigateToPaymentConfirm) {
                navigateToPaymentConfirm = true
            }
        }
    }

    fun onPaymentConfirmNavigated() {
        navigateToPaymentConfirm = false
    }

    fun togglePayMode() {
        setPayMode(!isPayModeActive)
    }

    fun updateHomeNormalMode(isNormalMode: Boolean) {
        isHomeNormalMode = isNormalMode
        if (!isNormalMode && isPayModeActive) {
            setPayMode(false)
        }
    }

    fun refreshWalletAvailability() {
        viewModelScope.launch {
            when (val result = walletRepository.getMyAll()) {
                is AppResult.Success -> {
                    hasWallets = result.data.isNotEmpty()
                    if (!hasWallets && isPayModeActive) {
                        setPayMode(false)
                    }
                }
                is AppResult.Error -> {
                    hasWallets = false
                    if (isPayModeActive) {
                        setPayMode(false)
                    }
                }
            }
        }
    }

    private fun setPayMode(active: Boolean) {
        isPayModeActive = active
        val componentName = ComponentName(context, PaymentHceService::class.java)
        val newState = if (active) {
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED
        } else {
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED
        }
        runCatching {
            context.packageManager.setComponentEnabledSetting(
                componentName,
                newState,
                PackageManager.DONT_KILL_APP
            )
        }
    }
}
