package com.krushkov.virtualwallet.domain.repositories

import com.krushkov.virtualwallet.domain.models.inputs.PaymentInput
import com.krushkov.virtualwallet.domain.models.outputs.transaction.Transaction
import com.krushkov.virtualwallet.domain.result.AppResult

interface PaymentRepository {

    suspend fun pay(input: PaymentInput): AppResult<Transaction>

}