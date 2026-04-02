package com.krushkov.virtualwallet.domain.repositories

import com.krushkov.virtualwallet.domain.models.inputs.TopUpInput
import com.krushkov.virtualwallet.domain.models.outputs.transaction.Transaction
import com.krushkov.virtualwallet.domain.result.AppResult

interface TopUpRepository {

    suspend fun topUp(input: TopUpInput): AppResult<Transaction>

}