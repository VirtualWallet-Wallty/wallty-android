package com.krushkov.virtualwallet.viewmodel.states

import com.krushkov.virtualwallet.domain.models.outputs.wallet.Wallet

data class MoveState(
    val fromWallet: Wallet? = null,
    val wallets: List<Wallet> = emptyList(),
    val selectedToWallet: Wallet? = null,
    val selectedCurrencyCode: String? = null,
    val isFromDropdownExpanded: Boolean = false,
    val isDropdownExpanded: Boolean = false,
    val currencySymbol: String = "",
    val amount: String = "",
    val isLoading: Boolean = false,
    val isSubmitLoading: Boolean = false,
    val isSuccess: Boolean = false
)
