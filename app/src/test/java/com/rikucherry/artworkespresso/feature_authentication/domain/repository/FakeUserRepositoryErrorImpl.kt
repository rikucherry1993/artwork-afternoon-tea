package com.rikucherry.artworkespresso.feature_authentication.domain.repository

import com.rikucherry.artworkespresso.common.data.remote.UserDto
import com.rikucherry.artworkespresso.feature_authentication.data.repository.UserRepository
import com.skydoves.sandwich.ApiResponse
import okhttp3.ResponseBody.Companion.toResponseBody

/**
 * Mock repository implementation of UserRepository
 *
 * For error responses
 */
class FakeUserRepositoryErrorImpl : UserRepository {

    /**
     * Mock up an error response from getCurrentUser Api
     *
     * error code: 400
     */
    override suspend fun getWhoAmI(token: String): ApiResponse<UserDto> {
        val errorResponse = ErrorResponse(
            error = "invalid_request",
            errorDescription = "fake message"
        )

        return ApiResponse.Failure.Error(retrofit2.Response
            .error(400, errorResponse.toString().toResponseBody()))
    }

}