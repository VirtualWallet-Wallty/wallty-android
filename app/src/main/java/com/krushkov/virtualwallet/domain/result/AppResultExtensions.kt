package com.krushkov.virtualwallet.domain.result

import com.krushkov.virtualwallet.domain.error.AppError

fun <T : Any, R : Any> AppResult<T>.map(transform: (T) -> R): AppResult<R> {
    return when (this) {
        is AppResult.Success -> AppResult.Success(transform(data))
        is AppResult.Error -> this
    }
}

fun <T : Any> AppResult<T>.onSuccess(action: (T) -> Unit): AppResult<T> {
    if (this is AppResult.Success) {
        action(data)
    }
    return this
}

fun <T : Any> AppResult<T>.onError(action: (AppError) -> Unit): AppResult<T> {
    if (this is AppResult.Error) {
        action(error)
    }
    return this
}

fun <T : Any, R : Any> AppResult<T>.fold(
    onSuccess: (T) -> R,
    onError: (AppError) -> R
): R {
    return when (this) {
        is AppResult.Success -> onSuccess(data)
        is AppResult.Error -> onError(error)
    }
}