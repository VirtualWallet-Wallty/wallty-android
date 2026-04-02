package com.krushkov.virtualwallet.data.repositories

import com.krushkov.virtualwallet.data.api.interfaces.TransferApi
import com.krushkov.virtualwallet.data.mappers.toDomain
import com.krushkov.virtualwallet.data.mappers.toRequest
import com.krushkov.virtualwallet.data.remote.ApiHandler.apiCall
import com.krushkov.virtualwallet.domain.models.inputs.TransferInput
import com.krushkov.virtualwallet.domain.models.outputs.transaction.Transaction
import com.krushkov.virtualwallet.domain.repositories.TransferRepository
import com.krushkov.virtualwallet.domain.result.AppResult
import com.krushkov.virtualwallet.domain.result.map
import javax.inject.Inject

class TransferRepositoryImpl @Inject constructor(
    private val api: TransferApi
) : TransferRepository {

    override suspend fun transfer(input: TransferInput): AppResult<Transaction> {
        return apiCall {
            api.transfer(input.toRequest())
        }.map { it.toDomain() }
    }
}