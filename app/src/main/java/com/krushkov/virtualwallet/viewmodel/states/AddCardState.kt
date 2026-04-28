package com.krushkov.virtualwallet.viewmodel.states

data class AddCardState(
    val cardHolder: String = "",
    val cardNumber: String = "",
    val expirationMonth: String = "",
    val expirationYear: String = "",
    val isMonthMenuExpanded: Boolean = false,
    val isYearMenuExpanded: Boolean = false,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false
)
