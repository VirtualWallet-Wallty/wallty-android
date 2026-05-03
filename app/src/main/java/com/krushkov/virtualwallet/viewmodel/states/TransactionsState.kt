package com.krushkov.virtualwallet.viewmodel.states

import com.krushkov.virtualwallet.domain.models.inputs.transaction.TransactionDirection
import com.krushkov.virtualwallet.domain.models.inputs.transaction.TransactionSortOrder
import com.krushkov.virtualwallet.domain.models.outputs.card.Card
import com.krushkov.virtualwallet.domain.models.outputs.currency.Currency
import com.krushkov.virtualwallet.domain.models.outputs.transaction.Transaction
import com.krushkov.virtualwallet.domain.models.outputs.transaction.TransactionType
import com.krushkov.virtualwallet.domain.models.outputs.wallet.Wallet
import java.time.LocalDate

data class TransactionsState(
    val transactions: List<Transaction> = emptyList(),
    val wallets: List<Wallet> = emptyList(),
    val cards: List<Card> = emptyList(),
    val currencies: Map<String, Currency> = emptyMap(),
    val isLoading: Boolean = false,
    val currentWalletId: Long? = null,
    val currentPage: Int = 0,
    val isEndReached: Boolean = false,
    val isMoreLoading: Boolean = false,
    val isFilterExpanded: Boolean = false,
    val filterDirection: TransactionDirection = TransactionDirection.ALL,
    val filterType: TransactionType? = null,
    val filterLabel: String? = null,
    val filterCardId: Long? = null,
    val filterDateFrom: LocalDate? = null,
    val filterDateTo: LocalDate? = null,
    val sortOrder: TransactionSortOrder = TransactionSortOrder.NEWEST
)
