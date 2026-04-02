package com.krushkov.virtualwallet.data.dtos.response.api

data class ApiResponse<T>(
    val success: Boolean,
    val path: String?,
    val message: String?,
    val data: T?,
    val errors: Map<String, String>,
    val timestamp: String
)