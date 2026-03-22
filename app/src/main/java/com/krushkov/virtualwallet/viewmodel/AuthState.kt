package com.krushkov.virtualwallet.viewmodel

import com.krushkov.virtualwallet.data.model.user.UserPrincipalResponse

sealed class AuthState {

    object Loading : AuthState()

    object Unauthenticated : AuthState()

    data class Authenticated(val user: UserPrincipalResponse) : AuthState()

    data class Error(val message: String) : AuthState()
}