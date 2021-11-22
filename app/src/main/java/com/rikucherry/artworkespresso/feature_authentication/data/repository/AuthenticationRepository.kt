package com.rikucherry.artworkespresso.feature_authentication.data.repository

import com.rikucherry.artworkespresso.feature_authentication.data.remote.data_source.UserTokenResponseDto

interface AuthenticationRepository {

    suspend fun getUserAccessToken(clientId: Int, clientSecret: String, grantType: String,
                                   code: String, redirectUri: String, ): UserTokenResponseDto

}