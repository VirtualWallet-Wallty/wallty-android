package com.krushkov.virtualwallet.data.remote

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.krushkov.virtualwallet.data.dtos.response.api.ApiResponse
import com.krushkov.virtualwallet.domain.error.AppError
import com.krushkov.virtualwallet.domain.result.AppResult
import retrofit2.Response
import java.io.IOException

object ApiHandler {
    private val gson = Gson()

    suspend fun <T : Any> apiCall(
        apiCall: suspend () -> Response<ApiResponse<T>>
    ): AppResult<T> {

        return try {
            val response = apiCall()
            val body = response.body()

            if (response.isSuccessful && body != null) {
                if (body.success) {
                    val data = body.data

                    if (data != null) {
                        AppResult.Success(data, body.message)
                    } else {
                        @Suppress("UNCHECKED_CAST")
                        AppResult.Success(Unit as T, body.message)
                    }
                } else {
                    AppResult.Error(
                        AppError.Api(
                            message = body.message ?: "Unknown API error",
                            statusCode = response.code(),
                            errors = body.errors ?: emptyMap()
                        )
                    )
                }
            } else {
                val errorBody = response.errorBody()?.string()
                val apiResponse = try {
                    val type = object : TypeToken<ApiResponse<T>>() {}.type
                    gson.fromJson<ApiResponse<T>>(errorBody, type)
                } catch (e: Exception) {
                    null
                }

                if (apiResponse != null) {
                    AppResult.Error(
                        AppError.Api(
                            message = apiResponse.message ?: "HTTP ${response.code()} error",
                            statusCode = response.code(),
                            errors = apiResponse.errors ?: emptyMap()
                        )
                    )
                } else {
                    AppResult.Error(
                        AppError.Api(
                            message = "HTTP ${response.code()} error",
                            statusCode = response.code()
                        )
                    )
                }
            }

        } catch (e: IOException) {
            AppResult.Error(AppError.Network())
        } catch (e: Exception) {
            AppResult.Error(AppError.Unknown(e.message ?: "Unexpected error"))
        }
    }
}