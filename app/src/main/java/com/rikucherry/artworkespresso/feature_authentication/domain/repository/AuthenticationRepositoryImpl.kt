package com.rikucherry.artworkespresso.feature_authentication.domain.repository

import android.net.Uri
import com.rikucherry.artworkespresso.common.Constants
import com.rikucherry.artworkespresso.feature_authentication.data.remote.AuthenticationApiService
import com.rikucherry.artworkespresso.feature_authentication.data.remote.data_source.UserTokenResponseDto
import com.rikucherry.artworkespresso.feature_authentication.data.repository.AuthenticationRepository
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val authApi: AuthenticationApiService
) : AuthenticationRepository {

    override fun formAuthorizeUri(
        responseType: String,
        clientId: String,
        redirectUri: String,
        scope: String?,
        state: String?,
        view: String?
    ): Uri {
        return Uri.parse(Constants.BASE_URL + Constants.BASE_AUTH_PATH)
            .buildUpon()
            .appendQueryParameter("response_type", responseType)
            .appendQueryParameter("client_id", clientId)
            .appendQueryParameter("redirect_uri", redirectUri)
            .appendQueryParameter("scope", scope ?: Constants.DEFAULT_SCOPE)
            .appendQueryParameter("state", state ?: "")
            .appendQueryParameter("view", view ?: "")
            .build()
    }

    override suspend fun getUserAccessToken(
        clientId: Int,
        clientSecret: String,
        grantType: String,
        code: String,
        redirectUri: String
    ): List<UserTokenResponseDto> {
        return authApi.getUserAccessToken(clientId, clientSecret, grantType, code, redirectUri)
    }
}