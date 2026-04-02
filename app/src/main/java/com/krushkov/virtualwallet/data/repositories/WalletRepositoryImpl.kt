package com.krushkov.virtualwallet.data.repositories

import com.krushkov.virtualwallet.data.api.interfaces.WalletApi
import com.krushkov.virtualwallet.data.mappers.toDomain
import com.krushkov.virtualwallet.data.mappers.toRequest
import com.krushkov.virtualwallet.data.remote.ApiHandler.apiCall
import com.krushkov.virtualwallet.domain.models.inputs.wallet.WalletCreateInput
import com.krushkov.virtualwallet.domain.models.outputs.wallet.Wallet
import com.krushkov.virtualwallet.domain.repositories.WalletRepository
import com.krushkov.virtualwallet.domain.result.AppResult
import com.krushkov.virtualwallet.domain.result.map
import javax.inject.Inject

class WalletRepositoryImpl @Inject constructor(
    private val api: WalletApi
) : WalletRepository {

    override suspend fun getById(id: Long): AppResult<Wallet> {
        return apiCall {
            api.getById(id)
        }.map { it.toDomain() }
    }

    override suspend fun getDefault(): AppResult<Wallet> {
        return apiCall {
            api.getDefault()
        }.map { it.toDomain() }
    }

    override suspend fun getMyAll(): AppResult<List<Wallet>> {
        return apiCall {
            api.getMyAll()
        }.map { list -> list.map { it.toDomain() } }
    }

    override suspend fun create(input: WalletCreateInput): AppResult<Wallet> {
        return apiCall {
            api.create(input.toRequest())
        }.map { it.toDomain() }
    }

    override suspend fun setDefault(id: Long): AppResult<Unit> {
        return apiCall {
            api.setDefault(id)
        }
    }
}