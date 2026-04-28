package com.krushkov.virtualwallet.data.dtos.response.user

data class UserLongResponse(
    val id: Long,
    val username: String,

    val firstName: String?,
    val lastName: String?,

    val email: String?,
    val phoneNumber: String?,
    val photoUrl: String?,

    val role: String?,
    val isBlocked: Boolean?
)
