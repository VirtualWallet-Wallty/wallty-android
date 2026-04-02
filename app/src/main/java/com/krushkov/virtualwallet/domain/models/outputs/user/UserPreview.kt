package com.krushkov.virtualwallet.domain.models.outputs.user

data class UserPreview(
    val id: Long,
    val username: String,

    val firstName: String?,
    val lastName: String?,

    val photoUrl: String?
)
