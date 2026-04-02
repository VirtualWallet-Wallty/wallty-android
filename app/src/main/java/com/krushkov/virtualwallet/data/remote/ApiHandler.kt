package com.krushkov.virtualwallet.data.remote

import com.krushkov.virtualwallet.data.dtos.response.api.ApiResponse
import com.krushkov.virtualwallet.domain.error.AppError
import com.krushkov.virtualwallet.domain.result.AppResult
import retrofit2.Response
import java.io.IOException

object ApiHandler {

    suspend fun <T : Any> apiCall(
        apiCall: suspend () -> Response<ApiResponse<T>>
    ): AppResult<T> {

        return try {
            val response = apiCall()

            if (response.isSuccessful) {
                val body = response.body()

                if (body != null && body.success) {
                    val data = body.data

                    if (data != null) {
                        AppResult.Success(data)
                    } else {
                        AppResult.Error(AppError.Unknown("Empty response data"))
                    }
                } else {
                    AppResult.Error(
                        AppError.Api(
                            message = body?.message ?: "Unknown API error",
                            statusCode = response.code()
                        )
                    )
                }
            } else {
                AppResult.Error(
                    AppError.Api(
                        message = "HTTP ${response.code()} error",
                        statusCode = response.code()
                    )
                )
            }

        } catch (e: IOException) {
            AppResult.Error(AppError.Network())
        } catch (e: Exception) {
            AppResult.Error(AppError.Unknown(e.message ?: "Unexpected error"))
        }
    }
}