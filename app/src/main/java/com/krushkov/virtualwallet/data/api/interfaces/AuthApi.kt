package com.krushkov.virtualwallet.data.api.interfaces

import com.krushkov.virtualwallet.data.dtos.response.api.ApiResponse
import com.krushkov.virtualwallet.data.dtos.request.auth.LoginRequest
import com.krushkov.virtualwallet.data.dtos.request.auth.RegisterRequest
import com.krushkov.virtualwallet.data.dtos.response.auth.UserPrincipalResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {

    @POST("/api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<ApiResponse<Unit>>

    @POST("/api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse<UserPrincipalResponse>>

    @POST("/api/auth/logout")
    suspend fun logout(): Response<ApiResponse<Unit>>

    @GET("/api/auth/me")
    suspend fun getMe(): Response<ApiResponse<UserPrincipalResponse>>
}