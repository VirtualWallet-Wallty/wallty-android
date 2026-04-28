package com.krushkov.virtualwallet.domain.error

fun AppError.getMessage(): String {
    return when (this) {
        is AppError.Api -> {
            val combinedErrors = if (errors.isNotEmpty()) {
                errors.values.joinToString("\n")
            } else ""

            if (combinedErrors.isNotBlank()) {
                if (message.isNotBlank() && message != "Validation failed.") {
                    "$message\n$combinedErrors"
                } else {
                    combinedErrors
                }
            } else {
                message
            }
        }

        is AppError.Validation -> {
            fieldErrors.values.joinToString("\n")
        }

        is AppError.Network -> "No internet connection"

        is AppError.Unknown -> "Something went wrong"
    }
}