package com.krushkov.virtualwallet.domain.repositories

import com.krushkov.virtualwallet.domain.models.inputs.transaction.TransactionFilterInput
import com.krushkov.virtualwallet.domain.models.outputs.transaction.Transaction
import com.krushkov.virtualwallet.domain.result.AppResult

interface TransactionRepository {

    suspend fun search(
        filter: TransactionFilterInput,
        page: Int,
        size: Int,
        sort: String? = "createdAt,desc"
    ): AppResult<List<Transaction>>

    suspend fun getById(id: Long): AppResult<Transaction>
}