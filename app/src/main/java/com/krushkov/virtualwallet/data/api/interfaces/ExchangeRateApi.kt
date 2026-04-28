package com.krushkov.virtualwallet.data.api.interfaces

import com.krushkov.virtualwallet.data.dtos.response.api.ApiResponse
import com.krushkov.virtualwallet.data.dtos.response.exchangerate.ExchangeRateResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import java.math.BigDecimal

interface ExchangeRateApi {

    @GET("/api/exchange-rate")
    suspend fun getAllRates(): Response<ApiResponse<List<ExchangeRateResponse>>>

    @GET("/api/exchange-rate/{baseCurrencyCode}")
    suspend fun getRates(@Path("baseCurrencyCode") baseCurrencyCode: String):
            Response<ApiResponse<List<ExchangeRateResponse>>>

    @GET("/api/exchange-rate/{fromCurrencyCode}/{toCurrencyCode}")
    suspend fun getRate(
        @Path("fromCurrencyCode") fromCurrencyCode: String,
        @Path("toCurrencyCode") toCurrencyCode: String
    ): Response<ApiResponse<BigDecimal>>
}