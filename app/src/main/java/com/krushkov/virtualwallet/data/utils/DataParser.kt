package com.krushkov.virtualwallet.data.utils

import java.time.LocalDateTime

fun String.toLocalDateTimeOrNull(): LocalDateTime? {
    return try {
        LocalDateTime.parse(this)
    } catch (e: Exception) {
        null
    }
}