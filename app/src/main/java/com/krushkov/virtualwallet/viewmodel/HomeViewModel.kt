package com.krushkov.virtualwallet.viewmodel

import android.util.Log
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
import com.krushkov.virtualwallet.viewmodel.states.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val walletRepository: WalletRepository,
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    var state by mutableStateOf(HomeState())
        private set

    init {
        Log.d("HOME", "VIEWMODEL INIT CALLED")
        loadHome()
    }

    fun loadHome() {
        viewModelScope.launch {

            state = state.copy(
                isLoading = true,
                error = null
            )

            try {
                Log.d("HOME", "LOAD HOME STARTED")

                val walletResult = withContext(Dispatchers.IO) {
                    walletRepository.getDefault()
                }

                Log.d("HOME", "Wallet result received: $walletResult")

                if (walletResult is AppResult.Success) {

                    val wallet = walletResult.data
                    Log.d("HOME", "Wallet loaded: ${wallet.id}")

                    val outgoingResult = withContext(Dispatchers.IO) {
                        transactionRepository.search(
                            TransactionFilterInput(senderWalletId = wallet.id),
                            0,
                            5
                        )
                    }

                    val incomingResult = withContext(Dispatchers.IO) {
                        transactionRepository.search(
                            TransactionFilterInput(recipientWalletId = wallet.id),
                            0,
                            5
                        )
                    }

                    Log.d("HOME", "Outgoing result: $outgoingResult")
                    Log.d("HOME", "Incoming result: $incomingResult")

                    if (outgoingResult is AppResult.Success &&
                        incomingResult is AppResult.Success
                    ) {

                        val combined = (outgoingResult.data + incomingResult.data)
                            .distinctBy { it.id }
                            .sortedByDescending { it.createdAt }
                            .take(5)

                        // ✅ SUCCESS STATE (clean)
                        state = state.copy(
                            wallet = wallet,
                            transactions = combined,
                            isLoading = false,
                            error = null
                        )

                    } else {
                        state = state.copy(
                            isLoading = false,
                            error = "Failed to load transactions"
                        )
                    }

                } else if (walletResult is AppResult.Error) {

                    state = state.copy(
                        isLoading = false,
                        error = walletResult.error.getMessage()
                    )
                }

            } catch (e: Exception) {
                Log.e("HOME", "loadHome crashed", e)

                state = state.copy(
                    isLoading = false,
                    error = e.message ?: "Unexpected error in loadHome"
                )
            }
        }
    }
}