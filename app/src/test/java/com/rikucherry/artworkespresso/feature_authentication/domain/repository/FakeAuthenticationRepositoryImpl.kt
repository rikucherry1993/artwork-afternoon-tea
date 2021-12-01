package com.rikucherry.artworkespresso.feature_authentication.domain.repository

import com.rikucherry.artworkespresso.common.Constants
import com.rikucherry.artworkespresso.feature_authentication.data.local.data_source.LoginInfoItem
import com.rikucherry.artworkespresso.feature_authentication.data.remote.data_source.ClientTokenResponseDto
import com.rikucherry.artworkespresso.feature_authentication.data.remote.data_source.UserTokenResponseDto
import com.rikucherry.artworkespresso.feature_authentication.data.repository.AuthenticationRepository
import com.skydoves.sandwich.ApiResponse

/**
 * Mock repository implementation of AuthenticationRepository
 *
 * For successful responses
 */
class FakeAuthenticationRepositoryImpl : AuthenticationRepository {

    /**
     * Mock up getting login info successfully
     */
    override suspend fun getLoginInfo(): LoginInfoItem? {
        return LoginInfoItem(
            id = 1,
            userId = "test",
            userName = "test",
            userIconUrl = "test.png",
            status = 2
        )
    }

    /**
     * Mock up inserting login info successfully
     */
    override suspend fun insertLogInfo(loginInfoItem: LoginInfoItem) {
        //Todo
    }

    /**
     * Mock up truncating login info successfully
     */
    override suspend fun truncateLoginInfo() {
        //Todo
    }

    /**
     * Mock up a successful response from getUserAccessToken Api
     */
    override suspend fun getUserAccessToken(
        clientId: Int,
        clientSecret: String,
        grantType: String,
        code: String,
        redirectUri: String
    ): ApiResponse<UserTokenResponseDto> {
       val userTokenResponseDto = UserTokenResponseDto(
           accessToken = "fake access token",
           expiresIn = 3600,
           refreshToken = "fake refresh token",
           scope = Constants.FULL_SCOPE,
           status = "success",
           tokenType = "Bearer"
       )

        return ApiResponse.Success(retrofit2.Response.success(userTokenResponseDto))
    }


    /**
     * Mock up a successful response from getClientAccessToken Api
     */
    override suspend fun getClientAccessToken(
        clientId: Int,
        clientSecret: String,
        grantType: String
    ): ApiResponse<ClientTokenResponseDto> {
        val clientTokenResponseDto = ClientTokenResponseDto(
            accessToken = "fake access token",
            expiresIn = 3600,
            status = "success",
            tokenType = "Bearer"
        )

        return ApiResponse.Success(retrofit2.Response.success(clientTokenResponseDto))
    }
}