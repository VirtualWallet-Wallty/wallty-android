package com.krushkov.virtualwallet.domain.result

import com.krushkov.virtualwallet.domain.error.AppError

sealed class AppResult<out T : Any> {

    data class Success<out T : Any>(
        val data: T
    ) : AppResult<T>()

    data class Error(
        val error: AppError
    ) : AppResult<Nothing>()
}
