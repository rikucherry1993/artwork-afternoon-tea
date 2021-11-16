package com.rikucherry.artworkespresso.feature_authentication.data.repository

import android.net.Uri
import com.rikucherry.artworkespresso.feature_authentication.data.remote.data_source.UserTokenResponseDto

interface AuthenticationRepository {

    /**
     * Form the authorization URL provided by server side
     * @see <a href="https://www.deviantart.com/developers/authentication">documentation</a>
     */
    fun formAuthorizeUri(responseType: String, clientId: String, redirectUri: String,
                         scope: String?, state: String?, view: String?): Uri


    suspend fun getUserAccessToken(clientId: Int, clientSecret: String, grantType: String,
                                   code: String, redirectUri: String, ): List<UserTokenResponseDto>

}