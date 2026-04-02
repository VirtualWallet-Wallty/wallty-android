package com.krushkov.virtualwallet.data.api.interfaces

import com.krushkov.virtualwallet.data.dtos.response.api.ApiResponse
import com.krushkov.virtualwallet.data.dtos.response.currency.CurrencyLongResponse
import com.krushkov.virtualwallet.data.dtos.response.currency.CurrencyShortResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CurrencyApi {

    @GET("/api/currencies/{targetCurrencyCode}")
    suspend fun getByCode(@Path("code") code: String):
            Response<ApiResponse<CurrencyLongResponse>>

    @GET("/api/currencies")
    suspend fun getAllActive(): Response<ApiResponse<List<CurrencyShortResponse>>>
}