package com.krushkov.virtualwallet.data.dtos.request.auth

data class LoginRequest(
    val identifier: String,
    val password: String
)