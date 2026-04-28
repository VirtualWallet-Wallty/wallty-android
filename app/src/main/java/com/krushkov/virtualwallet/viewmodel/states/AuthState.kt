package com.krushkov.virtualwallet.viewmodel.states

import com.krushkov.virtualwallet.domain.models.outputs.auth.AuthUser

data class AuthState(
    val isLoading: Boolean = false,
    val isCheckingSession: Boolean = true,
    val user: AuthUser? = null,
    val isLoggedIn: Boolean = false,
    val isRegistered: Boolean = false
)