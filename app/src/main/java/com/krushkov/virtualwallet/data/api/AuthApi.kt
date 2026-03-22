package com.krushkov.virtualwallet.data.api

import com.krushkov.virtualwallet.data.model.ApiResponse
import com.krushkov.virtualwallet.data.model.auth.LoginRequest
import com.krushkov.virtualwallet.data.model.user.UserPrincipalResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {
    @POST("/api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse<UserPrincipalResponse>>

    @POST("/api/auth/logout")
    suspend fun logout(): Response<ApiResponse<Unit>>

    @GET("/api/auth/me")
    suspend fun getMe(): Response<ApiResponse<UserPrincipalResponse>>
}