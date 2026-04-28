package com.krushkov.virtualwallet.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krushkov.virtualwallet.domain.error.getMessage
import com.krushkov.virtualwallet.domain.models.inputs.transaction.TransactionFilterInput
import com.krushkov.virtualwallet.domain.models.outputs.wallet.Wallet
import com.krushkov.virtualwallet.domain.repositories.TransactionRepository
import com.krushkov.virtualwallet.domain.repositories.WalletRepository
import com.krushkov.virtualwallet.domain.result.AppResult
import com.krushkov.virtualwallet.domain.result.map
import com.krushkov.virtualwallet.domain.result.fold
import com.krushkov.virtualwallet.ui.utils.NotificationManager
import com.krushkov.virtualwallet.viewmodel.states.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val walletRepository: WalletRepository,
    private val transactionRepository: TransactionRepository,
    private val currencyRepository: com.krushkov.virtualwallet.domain.repositories.CurrencyRepository,
    private val authRepository: com.krushkov.virtualwallet.domain.repositories.AuthRepository,
    private val notificationManager: NotificationManager
) : ViewModel() {

    var state by mutableStateOf(HomeState())
        private set

    init {
        loadCurrentUser()
        loadHome()
        loadCurrencies()
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            val result = authRepository.getMe()
            if (result is AppResult.Success) {
                state = state.copy(currentUser = result.data)
            }
        }
    }

    fun toggleAllWallets(visible: Boolean) {
        state = state.copy(isAllWalletsVisible = visible)
        if (visible) {
            loadAllWallets()
        }
    }

    fun refreshAll() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val currentWalletId = state.wallet?.id
            
            val walletsJob = launch { fetchAllWallets() }
            val homeJob = launch { fetchHome(currentWalletId) }
            val currenciesJob = launch { fetchCurrencies() }
            
            walletsJob.join()
            homeJob.join()
            currenciesJob.join()
            
            state = state.copy(isLoading = false)
        }
    }

    fun loadCurrencies() {
        viewModelScope.launch {
            fetchCurrencies()
        }
    }

    private suspend fun fetchCurrencies() {
        val result = currencyRepository.getAllActive()
        if (result is AppResult.Success) {
            state = state.copy(currencies = result.data.associateBy { it.code })
        }
    }

    fun loadAllWallets() {
        viewModelScope.launch {
            fetchAllWallets()
        }
    }

    private suspend fun fetchAllWallets() {
        val result = walletRepository.getMyAll()
        if (result is AppResult.Success) {
            state = state.copy(wallets = result.data)
        }
    }

    fun toggleEditingWallets() {
        if (state.isEditingWallets) {
            // Save logic: send request if there's a pending default wallet
            state.pendingDefaultWalletId?.let { pendingId ->
                // Check if the pending default is different from the current default
                // Note: we should compare with the actual default wallet, not just state.wallet
                val currentDefaultId = state.wallets.find { it.isDefault }?.id
                if (pendingId == currentDefaultId) {
                    state = state.copy(isEditingWallets = false, pendingDefaultWalletId = null)
                    return
                }

                viewModelScope.launch {
                    state = state.copy(isLoading = true)
                    val result = walletRepository.setDefault(pendingId)
                    if (result is AppResult.Success) {
                        // Refresh data synchronously in this coroutine to ensure order
                        val walletsJob = launch { fetchAllWallets() }
                        val homeJob = launch { fetchHome(pendingId) } // Load the new default as the hero wallet
                        
                        walletsJob.join()
                        homeJob.join()

                        state = state.copy(isEditingWallets = false, pendingDefaultWalletId = null, isLoading = false)
                        notificationManager.showSuccess("Default wallet updated")
                    } else if (result is AppResult.Error) {
                        state = state.copy(isLoading = false)
                        notificationManager.showError(result.error.getMessage())
                    }
                }
            } ?: run {
                state = state.copy(isEditingWallets = false)
            }
        } else {
            state = state.copy(isEditingWallets = true, pendingDefaultWalletId = null)
        }
    }

    fun setPendingDefaultWallet(walletId: Long) {
        state = state.copy(pendingDefaultWalletId = walletId)
    }

    fun cancelEditingWallets() {
        state = state.copy(isEditingWallets = false, pendingDefaultWalletId = null)
    }

    fun selectWallet(wallet: Wallet) {
        state = state.copy(
            wallet = wallet,
            isAllWalletsVisible = false
        )
        loadTransactions(wallet.id)
    }

    private fun loadTransactions(walletId: Long) {
        viewModelScope.launch {
            fetchTransactions(walletId)
        }
    }

    private suspend fun fetchTransactions(walletId: Long) = coroutineScope {
        state = state.copy(isLoading = true)
        
        val outgoingDeferred = async { 
            transactionRepository.search(
                filter = TransactionFilterInput(senderWalletId = walletId), 
                page = 0, 
                size = 7,
                sort = "createdAt,desc"
            ) 
        }
        val incomingDeferred = async { 
            transactionRepository.search(
                filter = TransactionFilterInput(recipientWalletId = walletId), 
                page = 0, 
                size = 7,
                sort = "createdAt,desc"
            ) 
        }

        val outgoingResult = outgoingDeferred.await()
        val incomingResult = incomingDeferred.await()
        
        val outgoing = if (outgoingResult is AppResult.Success) outgoingResult.data else emptyList()
        val incoming = if (incomingResult is AppResult.Success) incomingResult.data else emptyList()

        val combined = (outgoing + incoming)
            .distinctBy { it.id }
            .sortedByDescending { it.createdAt }
            .take(7)

        state = state.copy(
            transactions = combined,
            isLoading = false
        )
    }

    fun loadHome(preferredWalletId: Long? = null) {
        viewModelScope.launch {
            fetchHome(preferredWalletId)
        }
    }

    private suspend fun fetchHome(preferredWalletId: Long? = null) = coroutineScope {
        state = state.copy(isLoading = true)
        
        val walletResult: AppResult<Wallet> = if (preferredWalletId != null) {
            walletRepository.getMyAll().map { wallets ->
                wallets.find { it.id == preferredWalletId } ?: wallets.find { it.isDefault } ?: wallets.first()
            }
        } else {
            walletRepository.getDefault()
        }

        if (walletResult is AppResult.Success) {
            val wallet = walletResult.data
            
            val outgoingDeferred = async { 
                transactionRepository.search(
                    filter = TransactionFilterInput(senderWalletId = wallet.id), 
                    page = 0, 
                    size = 7,
                    sort = "createdAt,desc"
                ) 
            }
            val incomingDeferred = async { 
                transactionRepository.search(
                    filter = TransactionFilterInput(recipientWalletId = wallet.id), 
                    page = 0, 
                    size = 7,
                    sort = "createdAt,desc"
                ) 
            }

            val outgoingResult = outgoingDeferred.await()
            val incomingResult = incomingDeferred.await()

            val outgoing = if (outgoingResult is AppResult.Success) outgoingResult.data else emptyList()
            val incoming = if (incomingResult is AppResult.Success) incomingResult.data else emptyList()

            val combined = (outgoing + incoming)
                .distinctBy { it.id }
                .sortedByDescending { it.createdAt }
                .take(7)

            state = state.copy(
                wallet = wallet,
                transactions = combined,
                isLoading = false
            )
        } else if (walletResult is AppResult.Error) {
            state = state.copy(isLoading = false)
            if (walletResult.error.getMessage().isNotBlank()) {
                notificationManager.showError(walletResult.error.getMessage())
            }
        }
    }
}