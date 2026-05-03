package com.krushkov.virtualwallet.viewmodel.states

enum class NfcStatus { IDLE, WAITING, SENT, ERROR }

data class PosCurrencyOption(
    val code: String,
    val name: String? = null
) {
    val displayName: String
        get() = name?.takeIf { it.isNotBlank() }?.let { "$code - $it" } ?: code
}

data class PosTerminalState(
    val amount: String = "",
    val currencyCode: String = "",
    val currencyOptions: List<PosCurrencyOption> = emptyList(),
    val isCurrencyMenuExpanded: Boolean = false,
    val nfcStatus: NfcStatus = NfcStatus.IDLE,
    val isNfcAvailable: Boolean = true
)
