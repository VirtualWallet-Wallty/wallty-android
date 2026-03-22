package com.krushkov.virtualwallet.data.model.user

data class UserShortResponse(
    val id: Long,
    val username: String,
    val firstName: String,
    val lastName: String,
    val photoUrl: String
)
