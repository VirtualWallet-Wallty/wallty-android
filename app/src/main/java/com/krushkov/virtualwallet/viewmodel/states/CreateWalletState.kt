package com.krushkov.virtualwallet.viewmodel.states

import com.krushkov.virtualwallet.domain.models.outputs.currency.Currency

data class CreateWalletState(
    val name: String = "",
    val selectedCurrency: Currency? = null,
    val makeDefault: Boolean = false,
    val hasWallets: Boolean = false,
    val currencies: List<Currency> = emptyList(),
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isCurrencyMenuExpanded: Boolean = false
)
