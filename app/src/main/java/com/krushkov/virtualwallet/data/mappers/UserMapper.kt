package com.krushkov.virtualwallet.data.mappers

import com.krushkov.virtualwallet.data.dtos.request.card.CardCreateRequest
import com.krushkov.virtualwallet.data.dtos.request.user.UserUpdateRequest
import com.krushkov.virtualwallet.data.dtos.response.user.UserLongResponse
import com.krushkov.virtualwallet.data.dtos.response.user.UserShortResponse
import com.krushkov.virtualwallet.domain.models.inputs.card.CardCreateInput
import com.krushkov.virtualwallet.domain.models.inputs.user.UserUpdateInput
import com.krushkov.virtualwallet.domain.models.outputs.user.UserPreview
import com.krushkov.virtualwallet.domain.models.outputs.user.UserProfile

fun UserShortResponse.toDomain(): UserPreview {
    return UserPreview(
        id = id,
        username = username,
        firstName = firstName,
        lastName = lastName,
        photoUrl = photoUrl
    )
}

fun UserLongResponse.toDomain(): UserProfile {
    return UserProfile(
        id = id,
        username = username,
        firstName = firstName,
        lastName = lastName,
        email = email,
        phoneNumber = phoneNumber,
        photoUrl = photoUrl,
        role = role.toRoleType(),
        isBlocked = isBlocked,
        createdAt = createdAt,
        updatedAt = updatedAt,
        wallets = wallets.map { it.toDomain() },
        cards = cards.map { it.toDomain() }
    )
}

fun UserUpdateInput.toRequest(): UserUpdateRequest {
    return UserUpdateRequest(
        username = username,
        password = password,
        firstName = firstName,
        lastName = lastName,
        email = email,
        phoneNumber = phoneNumber,
        photoUrl = photoUrl
    )
}