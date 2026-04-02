package com.krushkov.virtualwallet.data.repositories

import com.krushkov.virtualwallet.data.api.interfaces.TransactionApi
import com.krushkov.virtualwallet.data.dtos.response.api.PageResponse
import com.krushkov.virtualwallet.data.dtos.response.transaction.TransactionShortResponse
import com.krushkov.virtualwallet.data.mappers.toDomain
import com.krushkov.virtualwallet.data.remote.ApiHandler.apiCall
import com.krushkov.virtualwallet.domain.models.inputs.transaction.TransactionFilterInput
import com.krushkov.virtualwallet.domain.models.outputs.transaction.Transaction
import com.krushkov.virtualwallet.domain.repositories.TransactionRepository
import com.krushkov.virtualwallet.domain.result.AppResult
import com.krushkov.virtualwallet.domain.result.map
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val api: TransactionApi
) : TransactionRepository {

    override suspend fun search(
        filter: TransactionFilterInput,
        page: Int,
        size: Int
    ): AppResult<List<Transaction>> {
        return apiCall {
            api.search(
                senderId = filter.senderId,
                recipientId = filter.recipientId,
                senderWalletId = filter.senderWalletId,
                recipientWalletId = filter.recipientWalletId,
                type = filter.type?.name,
                status = filter.status?.name,
                createdFrom = filter.createdFrom?.toString(),
                createdTo = filter.createdTo?.toString(),
                minAmount = filter.minAmount,
                maxAmount = filter.maxAmount,
                page = page,
                size = size
            )
        }.map { page -> page.content.map { it.toDomain() } }
    }

    override suspend fun getById(id: Long): AppResult<Transaction> {
        return apiCall {
            api.getById(id)
        }.map { it.toDomain() }
    }
}
