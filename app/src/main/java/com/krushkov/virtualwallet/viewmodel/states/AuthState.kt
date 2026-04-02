package com.krushkov.virtualwallet.viewmodel.states

import com.krushkov.virtualwallet.domain.models.outputs.auth.AuthUser

data class AuthState(
    val isLoading: Boolean = false,
    val user: AuthUser? = null,
    val error: String? = null,
    val isLoggedIn: Boolean = false
)