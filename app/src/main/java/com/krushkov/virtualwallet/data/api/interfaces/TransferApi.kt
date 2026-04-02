package com.krushkov.virtualwallet.data.api.interfaces

import com.krushkov.virtualwallet.data.dtos.request.TransferRequest
import com.krushkov.virtualwallet.data.dtos.response.api.ApiResponse
import com.krushkov.virtualwallet.data.dtos.response.transaction.TransactionLongResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface TransferApi {

    @POST("/api/transfer")
    suspend fun transfer(@Body request: TransferRequest):
            Response<ApiResponse<TransactionLongResponse>>
}