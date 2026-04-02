package com.krushkov.virtualwallet.data.repositories

import com.krushkov.virtualwallet.data.api.interfaces.CardApi
import com.krushkov.virtualwallet.data.mappers.toDomain
import com.krushkov.virtualwallet.data.mappers.toRequest
import com.krushkov.virtualwallet.data.remote.ApiHandler.apiCall
import com.krushkov.virtualwallet.domain.models.inputs.card.CardCreateInput
import com.krushkov.virtualwallet.domain.models.outputs.card.Card
import com.krushkov.virtualwallet.domain.repositories.CardRepository
import com.krushkov.virtualwallet.domain.result.AppResult
import com.krushkov.virtualwallet.domain.result.map
import javax.inject.Inject

class CardRepositoryImpl @Inject constructor(
    private val api: CardApi
) : CardRepository {

    override suspend fun getMyAll(): AppResult<List<Card>> {

        return apiCall {
            api.getMyAll()
        }.map { list -> list.map { it.toDomain() } }
    }

    override suspend fun getById(cardId: Long): AppResult<Card> {
        return apiCall {
            api.getById(cardId)
        }.map { it.toDomain() }
    }

    override suspend fun add(input: CardCreateInput): AppResult<Card> {
        return apiCall {
            api.add(input.toRequest())
        }.map { it.toDomain() }
    }

    override suspend fun remove(cardId: Long): AppResult<Unit> {
        return apiCall {
            api.remove(cardId)
        }
    }

    override suspend fun activate(cardId: Long): AppResult<Unit> {
        return apiCall {
            api.activate(cardId)
        }
    }

    override suspend fun deactivate(cardId: Long): AppResult<Unit> {
        return apiCall {
            api.deactivate(cardId)
        }
    }
}