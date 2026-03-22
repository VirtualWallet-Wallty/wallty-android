package com.krushkov.virtualwallet.data.model.auth

data class LoginRequest(
    val identifier: String,
    val password: String
)