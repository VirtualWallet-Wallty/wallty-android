package com.krushkov.virtualwallet.data.repositories

import com.krushkov.virtualwallet.data.api.interfaces.AuthApi
import com.krushkov.virtualwallet.data.dtos.request.auth.LoginRequest
import com.krushkov.virtualwallet.data.dtos.request.auth.RegisterRequest
import com.krushkov.virtualwallet.data.mappers.toDomain
import com.krushkov.virtualwallet.data.remote.ApiHandler.apiCall
import com.krushkov.virtualwallet.domain.models.outputs.auth.AuthUser
import com.krushkov.virtualwallet.domain.repositories.AuthRepository
import com.krushkov.virtualwallet.domain.result.AppResult
import com.krushkov.virtualwallet.domain.result.map
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi
) : AuthRepository {

    override suspend fun login(
        identifier: String,
        password: String
    ): AppResult<AuthUser> {
        return apiCall {
            api.login(LoginRequest(identifier, password))
        }.map { it.toDomain() }
    }

    override suspend fun register(
        username: String,
        password: String,
        firstName: String,
        lastName: String,
        email: String
    ): AppResult<Unit> {
        return apiCall {
            api.register(
                RegisterRequest(username, password, firstName, lastName, email)
            )
        }
    }

    override suspend fun logout(): AppResult<Unit> {
        return apiCall {
            api.logout()
        }
    }

    override suspend fun getMe(): AppResult<AuthUser> {
        return apiCall {
            api.getMe()
        }.map { it.toDomain() }
    }
}