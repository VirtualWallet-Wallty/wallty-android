package com.krushkov.virtualwallet.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krushkov.virtualwallet.domain.error.getMessage
import com.krushkov.virtualwallet.domain.models.inputs.transaction.TransactionFilterInput
import com.krushkov.virtualwallet.domain.repositories.TransactionRepository
import com.krushkov.virtualwallet.domain.repositories.WalletRepository
import com.krushkov.virtualwallet.domain.result.AppResult
import com.krushkov.virtualwallet.viewmodel.states.TransactionsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val walletRepository: WalletRepository,
    private val notificationManager: com.krushkov.virtualwallet.ui.utils.NotificationManager
) : ViewModel() {

    var state by mutableStateOf(TransactionsState())
        private set

    init {
        loadTransactions()
    }

    fun loadTransactions() {
        if (state.isLoading) return

        viewModelScope.launch {
            state = state.copy(isLoading = true, currentPage = 0, isEndReached = false)

            val walletResult = walletRepository.getDefault()

            if (walletResult is AppResult.Success) {
                val wallet = walletResult.data
                fetchPage(wallet.id, 0)
            } else if (walletResult is AppResult.Error) {
                state = state.copy(
                    isLoading = false
                )
                viewModelScope.launch {
                    notificationManager.showError(walletResult.error.getMessage())
                }
            }
        }
    }

    fun loadNextPage() {
        val walletId = state.currentWalletId ?: return
        if (state.isMoreLoading || state.isEndReached) return

        viewModelScope.launch {
            state = state.copy(isMoreLoading = true)
            fetchPage(walletId, state.currentPage + 1)
        }
    }

    private suspend fun fetchPage(walletId: Long, page: Int) {
        val pageSize = 15
        
        val outgoingDeferred = viewModelScope.async { 
            transactionRepository.search(
                filter = TransactionFilterInput(senderWalletId = walletId), 
                page = page, 
                size = pageSize,
                sort = "createdAt,desc"
            ) 
        }
        val incomingDeferred = viewModelScope.async { 
            transactionRepository.search(
                filter = TransactionFilterInput(recipientWalletId = walletId), 
                page = page, 
                size = pageSize,
                sort = "createdAt,desc"
            ) 
        }

        val outgoingResult = outgoingDeferred.await()
        val incomingResult = incomingDeferred.await()

        if (outgoingResult is AppResult.Success && incomingResult is AppResult.Success) {
            val newTransactions = (outgoingResult.data + incomingResult.data)
                .distinctBy { it.id }
                .sortedByDescending { it.createdAt }

            val isEnd = outgoingResult.data.size < pageSize && incomingResult.data.size < pageSize

            state = state.copy(
                transactions = if (page == 0) newTransactions else state.transactions + newTransactions,
                isLoading = false,
                isMoreLoading = false,
                currentPage = page,
                isEndReached = isEnd,
                currentWalletId = walletId
            )
        } else {
            state = state.copy(
                isLoading = false,
                isMoreLoading = false
            )
            viewModelScope.launch {
                notificationManager.showError("Failed to load transactions")
            }
        }
    }
}