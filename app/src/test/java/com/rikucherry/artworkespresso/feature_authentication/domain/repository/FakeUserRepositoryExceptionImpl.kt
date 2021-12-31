package com.rikucherry.artworkespresso.feature_authentication.domain.repository

import com.rikucherry.artworkespresso.common.data.remote.UserDto
import com.rikucherry.artworkespresso.feature_authentication.data.repository.UserRepository
import com.skydoves.sandwich.ApiResponse
import java.io.IOException

/**
 * Mock repository implementation of UserRepository
 *
 * For exceptions
 */
class FakeUserRepositoryExceptionImpl : UserRepository {

    /**
     * Mock up an exception from getCurrentUser Api
     *
     * Throws IOException
     */
    override suspend fun getWhoAmI(token: String): ApiResponse<UserDto> {
        val exception = IOException("Throw new IO exception")
        return ApiResponse.Failure.Exception(exception)
    }

}