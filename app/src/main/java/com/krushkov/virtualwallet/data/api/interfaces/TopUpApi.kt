package com.krushkov.virtualwallet.data.api.interfaces

import com.krushkov.virtualwallet.data.dtos.request.TopUpRequest
import com.krushkov.virtualwallet.data.dtos.response.api.ApiResponse
import com.krushkov.virtualwallet.data.dtos.response.transaction.TransactionLongResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface TopUpApi {

    @POST("/api/top-up")
    suspend fun topUp(@Body request: TopUpRequest): Response<ApiResponse<TransactionLongResponse>>
}