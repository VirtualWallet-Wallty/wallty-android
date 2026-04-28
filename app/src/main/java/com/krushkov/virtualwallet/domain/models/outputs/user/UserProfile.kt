package com.krushkov.virtualwallet.domain.models.outputs.user

data class UserProfile(
    val id: Long,
    val username: String,

    val firstName: String,
    val lastName: String,

    val email: String,
    val phoneNumber: String,
    val photoUrl: String?,

    val role: RoleType,
    val isBlocked: Boolean
)