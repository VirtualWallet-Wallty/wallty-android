package com.krushkov.virtualwallet.domain.repositories

import com.krushkov.virtualwallet.domain.models.inputs.TransferInput
import com.krushkov.virtualwallet.domain.models.outputs.transaction.Transaction
import com.krushkov.virtualwallet.domain.result.AppResult

interface TransferRepository {

    suspend fun transfer(input: TransferInput): AppResult<Transaction>

}