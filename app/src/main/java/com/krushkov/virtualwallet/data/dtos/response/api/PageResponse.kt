package com.krushkov.virtualwallet.data.dtos.response.api

data class PageResponse<T>(
    val content: List<T>,
    val page: PageInfo?
)

data class PageInfo(
    val size: Int,
    val number: Int,
    val totalElements: Long,
    val totalPages: Int
)
