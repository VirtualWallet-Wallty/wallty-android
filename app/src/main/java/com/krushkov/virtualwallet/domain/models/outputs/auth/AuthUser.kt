package com.krushkov.virtualwallet.domain.models.outputs.auth

import com.krushkov.virtualwallet.domain.models.outputs.user.RoleType

data class AuthUser(
    val id: Long,
    val username: String,
    val role: RoleType,
    val isBlocked: Boolean
)