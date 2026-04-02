package com.krushkov.virtualwallet.domain.repositories

import com.krushkov.virtualwallet.domain.models.inputs.user.UserFilterInput
import com.krushkov.virtualwallet.domain.models.inputs.user.UserUpdateInput
import com.krushkov.virtualwallet.domain.models.outputs.user.UserPreview
import com.krushkov.virtualwallet.domain.models.outputs.user.UserProfile
import com.krushkov.virtualwallet.domain.result.AppResult

interface UserRepository {

    suspend fun search(
        filter: UserFilterInput,
        page: Int,
        size: Int
    ): AppResult<List<UserPreview>>

    suspend fun getById(id: Long): AppResult<UserProfile>

    suspend fun update(input: UserUpdateInput): AppResult<UserProfile>
}