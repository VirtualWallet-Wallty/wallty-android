package com.krushkov.virtualwallet.domain.repositories

import com.krushkov.virtualwallet.domain.models.inputs.wallet.WalletCreateInput
import com.krushkov.virtualwallet.domain.models.outputs.wallet.Wallet
import com.krushkov.virtualwallet.domain.result.AppResult
import retrofit2.http.Body

interface WalletRepository {

    suspend fun getById(id: Long): AppResult<Wallet>

    suspend fun getDefault(): AppResult<Wallet>

    suspend fun getMyAll(): AppResult<List<Wallet>>

    suspend fun create(input: WalletCreateInput): AppResult<Wallet>

    suspend fun setDefault(id: Long): AppResult<Unit>
}