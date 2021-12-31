package com.rikucherry.artworkespresso.feature_authentication.domain.repository

import com.rikucherry.artworkespresso.common.data.remote.UserDto
import com.rikucherry.artworkespresso.feature_authentication.data.repository.UserRepository
import com.skydoves.sandwich.ApiResponse

/**
 * Mock repository implementation of UserRepository
 *
 * For successful responses
 */
class FakeUserRepositoryImpl : UserRepository {

    /**
     * Mock up a successful response from getCurrentUser Api
     */
    override suspend fun getWhoAmI(token: String): ApiResponse<UserDto> {
        val user = UserDto(
            userid = "id",
            username = "name",
            userIconUrl = "url",
            type = "type"
        )
        return ApiResponse.Success(retrofit2.Response.success(user))
    }

}