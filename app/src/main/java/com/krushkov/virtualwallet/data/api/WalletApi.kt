package com.krushkov.virtualwallet.data.api

import com.krushkov.virtualwallet.data.model.ApiResponse
import com.krushkov.virtualwallet.data.model.wallet.WalletLongResponse
import com.krushkov.virtualwallet.data.model.wallet.WalletShortResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface WalletApi {

    @GET("/api/wallets/my")
    suspend fun getMyWallets(): Response<ApiResponse<List<WalletShortResponse>>>

    @GET("/api/wallets/{id}")
    suspend fun getWalletById(@Path("id") id: Long): Response<ApiResponse<WalletLongResponse>>
}