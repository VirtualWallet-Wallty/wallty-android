package com.krushkov.virtualwallet.nfc

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object PaymentEventBus {
    private val _pendingPayment = MutableStateFlow<PendingPayment?>(null)
    val pendingPayment: StateFlow<PendingPayment?> = _pendingPayment.asStateFlow()

    fun post(payment: PendingPayment) {
        _pendingPayment.value = payment
    }

    fun consume() {
        _pendingPayment.value = null
    }
}
