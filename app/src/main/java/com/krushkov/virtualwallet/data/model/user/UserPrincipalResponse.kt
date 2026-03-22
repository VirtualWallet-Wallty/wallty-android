package com.krushkov.virtualwallet.data.model.user

data class UserPrincipalResponse(
    val id: Long,
    val username: String,
    val role: String,
    val isBlocked: Boolean
)
