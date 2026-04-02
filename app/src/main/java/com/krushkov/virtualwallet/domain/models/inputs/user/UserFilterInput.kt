package com.krushkov.virtualwallet.domain.models.inputs.user

import java.time.LocalDateTime

data class UserFilterInput(
    val username: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val phoneNumber: String? = null,

    val isBlocked: Boolean? = null,

    val createdFrom: LocalDateTime? = null,
    val createdTo: LocalDateTime? = null
)
