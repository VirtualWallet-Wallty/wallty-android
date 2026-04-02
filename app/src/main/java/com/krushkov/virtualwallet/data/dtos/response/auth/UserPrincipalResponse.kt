package com.krushkov.virtualwallet.data.dtos.response.auth

data class UserPrincipalResponse(
    val id: Long,
    val username: String,
    val role: String,
    val isBlocked: Boolean
)