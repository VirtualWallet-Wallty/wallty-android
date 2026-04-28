package com.krushkov.virtualwallet.viewmodel.states

import com.krushkov.virtualwallet.domain.models.outputs.card.Card
import com.krushkov.virtualwallet.domain.models.outputs.wallet.Wallet

data class TopUpState(
    val isWalletMode: Boolean,

    val wallet: Wallet? = null,
    val cards: List<Card> = emptyList(),
    val selectedCard: Card? = null,
    val isCardDropdownExpanded: Boolean = false,

    val card: Card? = null,
    val wallets: List<Wallet> = emptyList(),
    val selectedWallet: Wallet? = null,
    val isWalletDropdownExpanded: Boolean = false,

    val currencySymbol: String = "",
    val amount: String = "",
    val isLoading: Boolean = false,
    val isSubmitLoading: Boolean = false,
    val isSuccess: Boolean = false
)
