package com.krushkov.virtualwallet.data.api.interfaces

import com.krushkov.virtualwallet.data.dtos.request.card.CardCreateRequest
import com.krushkov.virtualwallet.data.dtos.response.api.ApiResponse
import com.krushkov.virtualwallet.data.dtos.response.card.CardLongResponse
import com.krushkov.virtualwallet.data.dtos.response.card.CardShortResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface CardApi {

    @GET("/api/cards/{targetCardId}")
    suspend fun getById(@Path("targetCardId") targetCardId: Long):
            Response<ApiResponse<CardLongResponse>>

    @GET("/api/cards/my")
    suspend fun getMyAll(): Response<ApiResponse<List<CardShortResponse>>>

    @POST("/api/cards")
    suspend fun add(@Body request: CardCreateRequest): Response<ApiResponse<CardLongResponse>>

    @PATCH("/api/cards/{targetCardId}/activate")
    suspend fun activate(@Path("targetCardId") targetCardId: Long):
            Response<ApiResponse<Unit>>

    @PATCH("/api/cards/{targetCardId}/deactivate")
    suspend fun deactivate(@Path("targetCardId") targetCardId: Long):
            Response<ApiResponse<Unit>>

    @DELETE("/api/cards/{targetCardId}")
    suspend fun remove(@Path("targetCardId") targetCardId: Long):
            Response<ApiResponse<Unit>>
}