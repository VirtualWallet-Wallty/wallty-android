package com.krushkov.virtualwallet.data.dtos.request.auth

data class RegisterRequest (
    val username: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val email: String
)