package com.krushkov.virtualwallet.data.repositories

import com.krushkov.virtualwallet.data.api.interfaces.UserApi
import com.krushkov.virtualwallet.data.dtos.request.user.UserUpdateRequest
import com.krushkov.virtualwallet.data.dtos.response.api.PageResponse
import com.krushkov.virtualwallet.data.dtos.response.user.UserShortResponse
import com.krushkov.virtualwallet.data.mappers.toDomain
import com.krushkov.virtualwallet.data.mappers.toRequest
import com.krushkov.virtualwallet.data.remote.ApiHandler.apiCall
import com.krushkov.virtualwallet.domain.error.AppError
import com.krushkov.virtualwallet.domain.models.inputs.user.UserFilterInput
import com.krushkov.virtualwallet.domain.models.inputs.user.UserUpdateInput
import com.krushkov.virtualwallet.domain.models.outputs.user.UserPreview
import com.krushkov.virtualwallet.domain.models.outputs.user.UserProfile
import com.krushkov.virtualwallet.domain.repositories.UserRepository
import com.krushkov.virtualwallet.domain.result.AppResult
import com.krushkov.virtualwallet.domain.result.map
import java.io.IOException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: UserApi
) : UserRepository {

    override suspend fun search(
        filter: UserFilterInput,
        page: Int,
        size: Int
    ): AppResult<List<UserPreview>> {
        return apiCall {
            api.search(
                username = filter.username,
                firstName = filter.firstName,
                lastName = filter.lastName,
                email = filter.email,
                phoneNumber = filter.phoneNumber,
                isBlocked = filter.isBlocked,
                createdFrom = filter.createdFrom?.toString(),
                createdTo = filter.createdTo?.toString(),
                page = page,
                size = size
            )
        }.map { page -> page.content.map { it.toDomain() } }
    }

    override suspend fun getById(id: Long): AppResult<UserProfile> {
        return apiCall {
            api.getById(id)
        }.map { it.toDomain() }
    }

    override suspend fun update(input: UserUpdateInput): AppResult<UserProfile> {
        return apiCall {
            api.update(input.toRequest())
        }.map { it.toDomain() }
    }
}