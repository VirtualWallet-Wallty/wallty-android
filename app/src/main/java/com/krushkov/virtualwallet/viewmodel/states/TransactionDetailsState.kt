package com.krushkov.virtualwallet.viewmodel.states

import com.krushkov.virtualwallet.domain.models.outputs.transaction.Transaction

data class TransactionDetailsState(
    val transaction: Transaction? = null,
    val ownedWalletIds: Set<Long> = emptySet(),
    val isLoading: Boolean = false
)
