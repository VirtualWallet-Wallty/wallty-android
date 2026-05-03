package com.krushkov.virtualwallet.viewmodel.states

import com.krushkov.virtualwallet.domain.models.outputs.wallet.Wallet
import com.krushkov.virtualwallet.nfc.PendingPayment

data class PaymentConfirmState(
    val pendingPayment: PendingPayment? = null,
    val wallet: Wallet? = null,
    val wallets: List<Wallet> = emptyList(),
    val isLoading: Boolean = false,
    val isDone: Boolean = false
)
