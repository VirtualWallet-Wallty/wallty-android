package com.krushkov.virtualwallet.data.repositories

import com.krushkov.virtualwallet.data.api.interfaces.TopUpApi
import com.krushkov.virtualwallet.data.dtos.response.api.ApiResponse
import com.krushkov.virtualwallet.data.mappers.toDomain
import com.krushkov.virtualwallet.data.mappers.toRequest
import com.krushkov.virtualwallet.data.remote.ApiHandler.apiCall
import com.krushkov.virtualwallet.domain.models.inputs.TopUpInput
import com.krushkov.virtualwallet.domain.models.outputs.transaction.Transaction
import com.krushkov.virtualwallet.domain.repositories.TopUpRepository
import com.krushkov.virtualwallet.domain.result.AppResult
import com.krushkov.virtualwallet.domain.result.map
import javax.inject.Inject

class TopUpRepositoryImpl @Inject constructor(
    private val api: TopUpApi
) : TopUpRepository {

    override suspend fun topUp(input: TopUpInput): AppResult<Transaction> {
        return apiCall {
            api.topUp(input.toRequest())
        }.map { it.toDomain() }
    }
}