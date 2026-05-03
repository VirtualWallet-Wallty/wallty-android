package com.krushkov.virtualwallet.viewmodel.states

import com.krushkov.virtualwallet.domain.models.outputs.card.Card
import com.krushkov.virtualwallet.domain.models.outputs.transaction.Transaction

data class CardsState(
    val isLoading: Boolean = false,
    val isTopUpsLoading: Boolean = false,
    val isTopUpLoading: Boolean = false,
    val isCardActionLoading: Boolean = false,
    val isRemoveLoading: Boolean = false,
    val cards: List<Card> = emptyList(),
    val selectedCard: Card? = null,
    val topUps: List<Transaction> = emptyList(),
    val isDeactivateDialogVisible: Boolean = false,
    val isActivateDialogVisible: Boolean = false,
    val isRemoveConfirmVisible: Boolean = false,
    val currencies: Map<String, com.krushkov.virtualwallet.domain.models.outputs.currency.Currency> = emptyMap()
)