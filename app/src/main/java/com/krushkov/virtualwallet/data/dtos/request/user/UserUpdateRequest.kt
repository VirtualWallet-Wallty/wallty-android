package com.krushkov.virtualwallet.data.dtos.request.user

data class UserUpdateRequest(
    val username: String?,
    val password: String,
    val firstName: String?,
    val lastName: String?,
    val email: String?,
    val phoneNumber: String?,
    val photoUrl: String?
)