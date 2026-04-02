package com.krushkov.virtualwallet.domain.repositories

import com.krushkov.virtualwallet.domain.models.inputs.card.CardCreateInput
import com.krushkov.virtualwallet.domain.models.outputs.card.Card
import com.krushkov.virtualwallet.domain.result.AppResult

interface CardRepository {

    suspend fun getMyAll(): AppResult<List<Card>>

    suspend fun getById(cardId: Long): AppResult<Card>

    suspend fun add(input: CardCreateInput): AppResult<Card>

    suspend fun remove(cardId: Long): AppResult<Unit>

    suspend fun activate(cardId: Long): AppResult<Unit>

    suspend fun deactivate(cardId: Long): AppResult<Unit>
}