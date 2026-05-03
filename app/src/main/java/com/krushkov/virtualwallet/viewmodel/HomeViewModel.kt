package com.krushkov.virtualwallet.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krushkov.virtualwallet.R
import com.krushkov.virtualwallet.domain.error.getMessage
import com.krushkov.virtualwallet.domain.models.inputs.transaction.TransactionFilterInput
import com.krushkov.virtualwallet.domain.models.outputs.wallet.Wallet
import com.krushkov.virtualwallet.domain.repositories.CardRepository
import com.krushkov.virtualwallet.domain.repositories.TransactionRepository
import com.krushkov.virtualwallet.domain.repositories.WalletRepository
import com.krushkov.virtualwallet.domain.result.AppResult
import com.krushkov.virtualwallet.domain.result.map
import com.krushkov.virtualwallet.domain.result.fold
import com.krushkov.virtualwallet.ui.utils.NotificationManager
import com.krushkov.virtualwallet.viewmodel.states.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val walletRepository: WalletRepository,
    private val cardRepository: CardRepository,
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
        viewModelScope.launch { fetchCardCount() }
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
            val cardsJob = launch { fetchCardCount() }

            walletsJob.join()
            homeJob.join()
            currenciesJob.join()
            cardsJob.join()

            state = state.copy(isLoading = false)
        }
    }

    private suspend fun fetchCardCount() {
        val result = cardRepository.getMyAll()
        if (result is AppResult.Success) {
            state = state.copy(cardCount = result.data.size)
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
            val currentDefaultId = state.wallets.find { it.isDefault }?.id
            val pendingId = state.pendingDefaultWalletId
            val hasPendingDefault = pendingId != null && pendingId != currentDefaultId

            val editingId = state.editingWalletId
            val editedName = state.editingWalletName.trim()
            val originalName = state.wallets.find { it.id == editingId }?.name
            val hasPendingRename = editingId != null && editedName.isNotBlank() && editedName != originalName

            if (!hasPendingDefault && !hasPendingRename) {
                state = state.copy(
                    isEditingWallets = false,
                    pendingDefaultWalletId = null,
                    editingWalletId = null,
                    editingWalletName = ""
                )
                return
            }

            viewModelScope.launch {
                state = state.copy(isLoading = true)
                var preferredId: Long? = pendingId ?: state.wallet?.id

                if (hasPendingRename) {
                    when (val result = walletRepository.update(editingId!!, editedName)) {
                        is AppResult.Error -> {
                            notificationManager.showError(result.error.getMessage())
                            state = state.copy(isLoading = false)
                            return@launch
                        }
                        else -> {}
                    }
                }

                if (hasPendingDefault) {
                    when (val result = walletRepository.setDefault(pendingId!!)) {
                        is AppResult.Success -> preferredId = pendingId
                        is AppResult.Error -> {
                            notificationManager.showError(result.error.getMessage())
                            state = state.copy(isLoading = false)
                            return@launch
                        }
                    }
                }

                val walletsJob = launch { fetchAllWallets() }
                val homeJob = launch { fetchHome(preferredId) }
                walletsJob.join()
                homeJob.join()

                state = state.copy(
                    isEditingWallets = false,
                    pendingDefaultWalletId = null,
                    editingWalletId = null,
                    editingWalletName = "",
                    isLoading = false
                )
                notificationManager.showSuccess(context.getString(R.string.msg_changes_saved))
            }
        } else {
            state = state.copy(isEditingWallets = true, pendingDefaultWalletId = null)
        }
    }

    fun startEditingWalletName(wallet: com.krushkov.virtualwallet.domain.models.outputs.wallet.Wallet) {
        if (state.editingWalletId == wallet.id) return
        state = state.copy(editingWalletId = wallet.id, editingWalletName = wallet.name)
    }

    fun onEditingWalletNameChange(name: String) {
        state = state.copy(editingWalletName = name)
    }

    fun clearEditingWalletName() {
        state = state.copy(editingWalletId = null, editingWalletName = "")
    }

    fun commitWalletNameEdit() {
        val editingId = state.editingWalletId ?: run {
            clearEditingWalletName()
            return
        }
        val editedName = state.editingWalletName.trim()
        val originalName = state.wallets.find { it.id == editingId }?.name

        if (editedName.isBlank() || editedName == originalName) {
            clearEditingWalletName()
            return
        }

        viewModelScope.launch {
            state = state.copy(isLoading = true)
            when (val result = walletRepository.update(editingId, editedName)) {
                is AppResult.Error -> notificationManager.showError(result.error.getMessage())
                else -> {}
            }
            fetchAllWallets()
            state = state.copy(editingWalletId = null, editingWalletName = "", isLoading = false)
        }
    }

    fun deleteWallet(walletId: Long) {
        viewModelScope.launch {
            when (val result = walletRepository.delete(walletId)) {
                is AppResult.Success -> {
                    val wasHeroWallet = state.wallet?.id == walletId
                    if (state.editingWalletId == walletId) {
                        state = state.copy(editingWalletId = null, editingWalletName = "")
                    }
                    val walletsJob = launch { fetchAllWallets() }
                    val homeJob = if (wasHeroWallet) launch { fetchHome() } else null
                    walletsJob.join()
                    homeJob?.join()
                    notificationManager.showSuccess(result.message ?: context.getString(R.string.msg_wallet_deleted))
                }
                is AppResult.Error -> notificationManager.showError(result.error.getMessage())
            }
        }
    }

    fun setPendingDefaultWallet(walletId: Long) {
        state = state.copy(pendingDefaultWalletId = walletId)
    }

    fun cancelEditingWallets() {
        state = state.copy(
            isEditingWallets = false,
            pendingDefaultWalletId = null,
            editingWalletId = null,
            editingWalletName = ""
        )
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
        
        val walletsResult = walletRepository.getMyAll()

        if (walletsResult is AppResult.Success) {
            val wallets = walletsResult.data
            val wallet = wallets.find { it.id == preferredWalletId }
                ?: wallets.find { it.isDefault }
                ?: wallets.firstOrNull()

            if (wallet == null) {
                state = state.copy(
                    wallet = null,
                    wallets = emptyList(),
                    transactions = emptyList(),
                    isAllWalletsVisible = false,
                    isEditingWallets = false,
                    pendingDefaultWalletId = null,
                    editingWalletId = null,
                    editingWalletName = "",
                    isLoading = false
                )
                return@coroutineScope
            }
            
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
                wallets = wallets,
                transactions = combined,
                isLoading = false
            )
        } else if (walletsResult is AppResult.Error) {
            state = state.copy(isLoading = false)
            if (walletsResult.error.getMessage().isNotBlank()) {
                notificationManager.showError(walletsResult.error.getMessage())
            }
        }
    }
}
