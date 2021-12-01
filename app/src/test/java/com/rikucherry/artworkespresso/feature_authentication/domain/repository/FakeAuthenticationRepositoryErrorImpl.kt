package com.rikucherry.artworkespresso.feature_authentication.domain.repository

import com.rikucherry.artworkespresso.feature_authentication.data.local.data_source.LoginInfoItem
import com.rikucherry.artworkespresso.feature_authentication.data.remote.data_source.ClientTokenResponseDto
import com.rikucherry.artworkespresso.feature_authentication.data.remote.data_source.UserTokenResponseDto
import com.rikucherry.artworkespresso.feature_authentication.data.repository.AuthenticationRepository
import com.skydoves.sandwich.ApiResponse
import okhttp3.ResponseBody.Companion.toResponseBody

/**
 * Mock repository implementation of AuthenticationRepository
 *
 * For error responses
 */
class FakeAuthenticationRepositoryErrorImpl : AuthenticationRepository {

    override suspend fun getLoginInfo(): LoginInfoItem? {
        return LoginInfoItem(
            id = 1,
            userId = "test",
            userName = "test",
            userIconUrl = "test.png",
            status = 5
        )
    }

    override suspend fun insertLogInfo(loginInfoItem: LoginInfoItem) {
        //Todo
    }

    override suspend fun truncateLoginInfo() {
        //Todo
    }

    /**
     * Mock up an error response from getUserAccessToken Api
     *
     * error code: 400
     */
    override suspend fun getUserAccessToken(
        clientId: Int,
        clientSecret: String,
        grantType: String,
        code: String,
        redirectUri: String
    ): ApiResponse<UserTokenResponseDto> {
        val errorResponse = ErrorResponse(
            error = "invalid_request",
            errorDescription = "fake message"
        )

        return ApiResponse.Failure.Error(retrofit2.Response
            .error(400, errorResponse.toString().toResponseBody()))
    }


    /**
     * Mock up an error response from getClientAccessToken Api
     *
     * error code: 400
     */
    override suspend fun getClientAccessToken(
        clientId: Int,
        clientSecret: String,
        grantType: String
    ): ApiResponse<ClientTokenResponseDto> {
        val errorResponse = ErrorResponse(
            error = "invalid_request",
            errorDescription = "fake message."
        )

        return ApiResponse.Failure.Error(retrofit2.Response
            .error(400, errorResponse.toString().toResponseBody()))
    }
}

data class ErrorResponse(
    val error: String,
    val errorDescription: String
)