package com.krushkov.virtualwallet.viewmodel.states

import com.krushkov.virtualwallet.domain.models.outputs.transaction.Transaction

data class TransactionsState(
    val transactions: List<Transaction> = emptyList(),
    val isLoading: Boolean = false,
    val currentWalletId: Long? = null,
    val currentPage: Int = 0,
    val isEndReached: Boolean = false,
    val isMoreLoading: Boolean = false
)