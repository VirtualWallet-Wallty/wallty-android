package com.krushkov.virtualwallet.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krushkov.virtualwallet.R
import com.krushkov.virtualwallet.domain.error.getMessage
import com.krushkov.virtualwallet.domain.models.inputs.card.CardCreateInput
import com.krushkov.virtualwallet.domain.repositories.CardRepository
import com.krushkov.virtualwallet.domain.result.AppResult
import com.krushkov.virtualwallet.ui.utils.NotificationManager
import com.krushkov.virtualwallet.viewmodel.states.AddCardState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCardViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val cardRepository: CardRepository,
    private val notificationManager: NotificationManager
) : ViewModel() {

    var state by mutableStateOf(AddCardState())
        private set

    fun onCardHolderChange(value: String) {
        state = state.copy(cardHolder = value)
    }

    fun onCardNumberChange(value: String) {
        if (value.all { it.isDigit() }) state = state.copy(cardNumber = value)
    }

    fun onMonthSelect(month: String) {
        state = state.copy(expirationMonth = month, isMonthMenuExpanded = false)
    }

    fun onYearSelect(year: String) {
        state = state.copy(expirationYear = year, isYearMenuExpanded = false)
    }

    fun toggleMonthMenu(expanded: Boolean) {
        state = state.copy(isMonthMenuExpanded = expanded)
    }

    fun toggleYearMenu(expanded: Boolean) {
        state = state.copy(isYearMenuExpanded = expanded)
    }

    fun addCard() {
        val expiration = "${state.expirationMonth}/${state.expirationYear}"
        if (state.cardHolder.isBlank() || state.cardNumber.isBlank() ||
            state.expirationMonth.isBlank() || state.expirationYear.isBlank()
        ) {
            viewModelScope.launch { notificationManager.showError(context.getString(R.string.msg_card_fields_required)) }
            return
        }
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            when (val result = cardRepository.add(
                CardCreateInput(
                    cardHolder = state.cardHolder,
                    cardNumber = state.cardNumber,
                    expiration = expiration
                )
            )) {
                is AppResult.Success -> {
                    notificationManager.showSuccess(context.getString(R.string.msg_card_added))
                    state = state.copy(isLoading = false, isSuccess = true)
                }
                is AppResult.Error -> {
                    notificationManager.showError(result.error.getMessage())
                    state = state.copy(isLoading = false)
                }
            }
        }
    }

    fun resetSuccess() {
        state = state.copy(isSuccess = false)
    }
}
