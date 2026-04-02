package com.krushkov.virtualwallet.domain.error

sealed class AppError {

    data class Api(
        val message: String,
        val statusCode: Int
    ) : AppError()

    data class Validation(
        val message: String,
        val fieldErrors: Map<String, String>
    ) : AppError()

    data class Network(
        val message: String = "No internet connection"
    ) : AppError()

    data class Unknown(
        val message: String = "Unexpected error occurred"
    ) : AppError()

}