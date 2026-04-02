package com.krushkov.virtualwallet.data.api.interfaces

import com.krushkov.virtualwallet.data.dtos.request.user.UserUpdateRequest
import com.krushkov.virtualwallet.data.dtos.response.api.ApiResponse
import com.krushkov.virtualwallet.data.dtos.response.api.PageResponse
import com.krushkov.virtualwallet.data.dtos.response.user.UserLongResponse
import com.krushkov.virtualwallet.data.dtos.response.user.UserShortResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApi {

    @GET("/api/users/{targetUserId}")
    suspend fun getById(@Path("targetUserId") targetUserId: Long):
            Response<ApiResponse<UserLongResponse>>

    @GET("/api/users")
    suspend fun search(
        @Query("username") username: String?,
        @Query("firstName") firstName: String?,
        @Query("lastName") lastName: String?,
        @Query("email") email: String?,
        @Query("phoneNumber") phoneNumber: String?,
        @Query("isBlocked") isBlocked: Boolean?,
        @Query("createdFrom") createdFrom: String?,
        @Query("createdTo") createdTo: String?,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): Response<ApiResponse<PageResponse<UserShortResponse>>>

    @PUT("/api/users")
    suspend fun update(@Body request: UserUpdateRequest): Response<ApiResponse<UserLongResponse>>
}