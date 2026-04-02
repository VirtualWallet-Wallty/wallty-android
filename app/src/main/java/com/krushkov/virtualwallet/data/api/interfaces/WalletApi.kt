package com.krushkov.virtualwallet.data.api.interfaces

import com.krushkov.virtualwallet.data.dtos.request.wallet.WalletCreateRequest
import com.krushkov.virtualwallet.data.dtos.response.api.ApiResponse
import com.krushkov.virtualwallet.data.dtos.response.api.PageResponse
import com.krushkov.virtualwallet.data.dtos.response.wallet.WalletLongResponse
import com.krushkov.virtualwallet.data.dtos.response.wallet.WalletShortResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.math.BigDecimal

interface WalletApi {

    @GET("/api/wallets/{targetWalletId}")
    suspend fun getById(@Path("targetWalletId") targetWalletId: Long):
            Response<ApiResponse<WalletLongResponse>>

    @GET("/api/wallets/default")
    suspend fun getDefault():
            Response<ApiResponse<WalletLongResponse>>

    @GET("/api/wallets/my")
    suspend fun getMyAll():
            Response<ApiResponse<List<WalletShortResponse>>>

    @POST("/api/wallets")
    suspend fun create(@Body request: WalletCreateRequest):
            Response<ApiResponse<WalletLongResponse>>

    @PATCH("/api/wallets/{targetWalletId}/set-default")
    suspend fun setDefault(@Path("targetWalletId") targetWalletId: Long):
            Response<ApiResponse<Unit>>

}