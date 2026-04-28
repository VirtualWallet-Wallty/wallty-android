package com.krushkov.virtualwallet.viewmodel.states

import com.krushkov.virtualwallet.domain.models.outputs.auth.AuthUser
import com.krushkov.virtualwallet.domain.models.outputs.currency.Currency
import com.krushkov.virtualwallet.domain.models.outputs.transaction.Transaction
import com.krushkov.virtualwallet.domain.models.outputs.wallet.Wallet

data class HomeState(
    val currentUser: AuthUser? = null,
    val wallet: Wallet? = null,
    val wallets: List<Wallet> = emptyList(),
    val transactions: List<Transaction> = emptyList(),
    val currencies: Map<String, Currency> = emptyMap(),
    val isAllWalletsVisible: Boolean = false,
    val isEditingWallets: Boolean = false,
    val pendingDefaultWalletId: Long? = null,
    val isLoading: Boolean = false
)