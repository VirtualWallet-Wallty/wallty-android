package com.krushkov.virtualwallet.domain.error

fun AppError.getMessage(): String {
    return when (this) {
        is AppError.Api -> message

        is AppError.Validation -> {
            fieldErrors.values.joinToString("\n")
        }

        is AppError.Network -> "No internet connection"

        is AppError.Unknown -> "Something went wrong"
    }
}