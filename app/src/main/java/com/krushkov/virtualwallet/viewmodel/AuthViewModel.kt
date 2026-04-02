package com.krushkov.virtualwallet.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krushkov.virtualwallet.domain.error.getMessage
import com.krushkov.virtualwallet.domain.repositories.AuthRepository
import com.krushkov.virtualwallet.domain.result.fold
import com.krushkov.virtualwallet.domain.result.onError
import com.krushkov.virtualwallet.domain.result.onSuccess
import com.krushkov.virtualwallet.viewmodel.states.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    var state by mutableStateOf(AuthState())
        private set

    fun login(identifier: String, password: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)

            authRepository.login(identifier, password).fold(
                onSuccess = {
                    state = state.copy(
                        isLoading = false,
                        user = it,
                        isLoggedIn = true
                    )
                },
                onError = {
                    state = state.copy(
                        isLoading = false,
                        error = it.getMessage()
                    )
                }
            )
        }
    }

    fun register(
        username: String,
        password: String,
        firstName: String,
        lastName: String,
        email: String
    ) {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)

            authRepository.register(
                username, password, firstName, lastName, email
            ).fold(
                onSuccess = {
                    state = state.copy(isLoading = false)
                },
                onError = {
                    state = state.copy(
                        isLoading = false,
                        error = it.getMessage()
                    )
                }
            )
        }
    }
}