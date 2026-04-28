package com.krushkov.virtualwallet.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krushkov.virtualwallet.data.api.SessionManager
import com.krushkov.virtualwallet.domain.error.getMessage
import com.krushkov.virtualwallet.domain.repositories.AuthRepository
import com.krushkov.virtualwallet.domain.result.fold
import com.krushkov.virtualwallet.ui.utils.NotificationManager
import com.krushkov.virtualwallet.viewmodel.states.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val notificationManager: NotificationManager
) : ViewModel() {

    var state by mutableStateOf(AuthState())
        private set

    init {
        SessionManager.setOnLogout {
            handleSessionExpired()
        }
        checkSession()
    }

    private fun checkSession() {
        viewModelScope.launch {
            state = state.copy(isLoading = true, isCheckingSession = true)
            authRepository.getMe().fold(
                onSuccess = {
                    state = state.copy(
                        isLoading = false,
                        isCheckingSession = false,
                        user = it,
                        isLoggedIn = true
                    )
                },
                onError = {
                    state = state.copy(
                        isLoading = false,
                        isCheckingSession = false,
                        isLoggedIn = false
                    )
                }
            )
        }
    }

    private fun handleSessionExpired() {
        state = AuthState(
            isLoggedIn = false,
            isCheckingSession = false,
            user = null
        )
        viewModelScope.launch {
            notificationManager.showError("Session expired")
        }
    }

    fun login(identifier: String, password: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)

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
                        isLoading = false
                    )
                    viewModelScope.launch {
                        notificationManager.showError(it.getMessage())
                    }
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
            state = state.copy(isLoading = true, isRegistered = false)

            authRepository.register(
                username, password, firstName, lastName, email
            ).fold(
                onSuccess = {
                    state = state.copy(
                        isLoading = false,
                        isRegistered = true
                    )
                    viewModelScope.launch {
                        notificationManager.showSuccess("Registration successful!")
                    }
                },
                onError = {
                    state = state.copy(
                        isLoading = false
                    )
                    viewModelScope.launch {
                        notificationManager.showError(it.getMessage())
                    }
                }
            )
        }
    }

    fun resetRegistrationState() {
        state = state.copy(isRegistered = false)
    }

    fun logout() {
        viewModelScope.launch {
            try {
                authRepository.logout()
            } catch (e: Exception) {
                // Ignore network errors on logout
            }
            state = AuthState(isLoggedIn = false, isCheckingSession = false)
        }
    }
}