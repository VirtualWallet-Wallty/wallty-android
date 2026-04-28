package com.krushkov.virtualwallet.data.api.interfaces

import com.krushkov.virtualwallet.data.dtos.response.api.ApiResponse
import com.krushkov.virtualwallet.data.dtos.response.api.PageResponse
import com.krushkov.virtualwallet.data.dtos.response.transaction.TransactionLongResponse
import com.krushkov.virtualwallet.data.dtos.response.transaction.TransactionShortResponse
import com.krushkov.virtualwallet.domain.models.outputs.transaction.TransactionStatus
import com.krushkov.virtualwallet.domain.models.outputs.transaction.TransactionType
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.math.BigDecimal
import java.time.LocalDateTime

interface TransactionApi {

    @GET("/api/transactions/{targetTransactionId}")
    suspend fun getById(@Path("targetTransactionId") targetTransactionId: Long):
            Response<ApiResponse<TransactionLongResponse>>

    @GET("/api/transactions")
    suspend fun search(
        @Query("label") label: String?,
        @Query("senderId") senderId: Long?,
        @Query("recipientId") recipientId: Long?,
        @Query("senderWalletId") senderWalletId: Long?,
        @Query("recipientWalletId") recipientWalletId: Long?,
        @Query("type") type: String?,
        @Query("status") status: String?,
        @Query("senderCurrencyCode") senderCurrencyCode: String?,
        @Query("recipientCurrencyCode") recipientCurrencyCode: String?,
        @Query("minSenderAmount") minSenderAmount: BigDecimal?,
        @Query("maxSenderAmount") maxSenderAmount: BigDecimal?,
        @Query("minRecipientAmount") minRecipientAmount: BigDecimal?,
        @Query("maxRecipientAmount") maxRecipientAmount: BigDecimal?,
        @Query("externalReference") externalReference: String?,
        @Query("createdFrom") createdFrom: String?,
        @Query("createdTo") createdTo: String?,
        @Query("sort") sort: String?,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): Response<ApiResponse<PageResponse<TransactionShortResponse>>>
}