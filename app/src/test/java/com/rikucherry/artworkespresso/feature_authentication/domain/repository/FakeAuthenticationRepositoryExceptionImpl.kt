package com.rikucherry.artworkespresso.feature_authentication.domain.repository

import com.rikucherry.artworkespresso.feature_authentication.data.remote.data_source.ClientTokenResponseDto
import com.rikucherry.artworkespresso.feature_authentication.data.remote.data_source.UserTokenResponseDto
import com.rikucherry.artworkespresso.feature_authentication.data.repository.AuthenticationRepository
import com.skydoves.sandwich.ApiResponse
import java.io.IOException

/**
 * Mock repository implementation of AuthenticationRepository
 *
 * For exceptions
 */
class FakeAuthenticationRepositoryExceptionImpl : AuthenticationRepository {

    /**
     * Mock up an exception response from getUserAccessToken Api
     *
     * Throws IOException
     */
    override suspend fun getUserAccessToken(
        clientId: Int,
        clientSecret: String,
        grantType: String,
        code: String,
        redirectUri: String
    ): ApiResponse<UserTokenResponseDto> {
        val exception = IOException("Throw new IO exception")
        return ApiResponse.Failure.Exception(exception)
    }


    /**
     * Mock up an exception response from getClientAccessToken Api
     *
     * Throws IOException
     */
    override suspend fun getClientAccessToken(
        clientId: Int,
        clientSecret: String,
        grantType: String
    ): ApiResponse<ClientTokenResponseDto> {
        val exception = IOException("Throw new IO exception")
        return ApiResponse.Failure.Exception(exception)
    }
}