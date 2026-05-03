package com.krushkov.virtualwallet.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krushkov.virtualwallet.domain.repositories.TransactionRepository
import com.krushkov.virtualwallet.domain.result.AppResult
import com.krushkov.virtualwallet.viewmodel.states.TransactionDetailsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    var state by mutableStateOf(TransactionDetailsState(isLoading = true))
        private set

    private val transactionId: Long? = savedStateHandle.get<Long>("transactionId")

    init {
        loadTransaction()
    }

    private fun loadTransaction() {
        val id = transactionId
        if (id == null) {
            state = state.copy(isLoading = false)
            return
        }

        viewModelScope.launch {
            state = state.copy(isLoading = true)
            state = when (val result = transactionRepository.getById(id)) {
                is AppResult.Success -> state.copy(transaction = result.data, isLoading = false)
                is AppResult.Error -> state.copy(isLoading = false)
            }
        }
    }
}
