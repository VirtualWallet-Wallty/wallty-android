package com.krushkov.virtualwallet.domain.models.inputs.user

data class UserUpdateInput(
    val username: String?,
    val password: String,
    val firstName: String?,
    val lastName: String?,
    val email: String?,
    val phoneNumber: String?,
    val photoUrl: String?
)
