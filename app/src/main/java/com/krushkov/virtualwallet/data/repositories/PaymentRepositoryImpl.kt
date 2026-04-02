package com.krushkov.virtualwallet.data.repositories

import com.krushkov.virtualwallet.data.api.interfaces.PaymentApi
import com.krushkov.virtualwallet.data.mappers.toDomain
import com.krushkov.virtualwallet.data.mappers.toRequest
import com.krushkov.virtualwallet.data.remote.ApiHandler.apiCall
import com.krushkov.virtualwallet.domain.models.inputs.PaymentInput
import com.krushkov.virtualwallet.domain.models.outputs.transaction.Transaction
import com.krushkov.virtualwallet.domain.repositories.PaymentRepository
import com.krushkov.virtualwallet.domain.result.AppResult
import com.krushkov.virtualwallet.domain.result.map
import javax.inject.Inject

class PaymentRepositoryImpl @Inject constructor(
    private val api: PaymentApi
) : PaymentRepository {

    override suspend fun pay(input: PaymentInput): AppResult<Transaction> {
        return apiCall {
            api.pay(input.toRequest())
        }.map { it.toDomain() }
    }
}