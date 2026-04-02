package com.krushkov.virtualwallet.data.mappers

import com.krushkov.virtualwallet.data.dtos.response.auth.UserPrincipalResponse
import com.krushkov.virtualwallet.domain.models.outputs.auth.AuthUser

fun UserPrincipalResponse.toDomain(): AuthUser {
    return AuthUser(
        id = id,
        username = username,
        role = role.toRoleType(),
        isBlocked = isBlocked
    )
}