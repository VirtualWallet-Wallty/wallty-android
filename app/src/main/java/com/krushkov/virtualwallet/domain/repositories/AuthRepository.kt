package com.krushkov.virtualwallet.domain.repositories

import com.krushkov.virtualwallet.domain.models.outputs.auth.AuthUser
import com.krushkov.virtualwallet.domain.result.AppResult

interface AuthRepository {

    suspend fun login(
        identifier: String,
        password: String
    ): AppResult<AuthUser>

    suspend fun register(
        username: String,
        password: String,
        firstName: String,
        lastName: String,
        email: String
    ): AppResult<Unit>

    suspend fun logout(): AppResult<Unit>

    suspend fun getMe(): AppResult<AuthUser>
}