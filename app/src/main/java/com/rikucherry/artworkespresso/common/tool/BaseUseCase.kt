package com.rikucherry.artworkespresso.common.tool

import com.rikucherry.artworkespresso.BuildConfig
import com.rikucherry.artworkespresso.ISecrets
import com.rikucherry.artworkespresso.common.Constants
import com.rikucherry.artworkespresso.feature_authentication.data.repository.AuthenticationRepository
import com.skydoves.sandwich.StatusCode
import com.skydoves.sandwich.onSuccess
import timber.log.Timber

open class BaseUseCase (
    private val authRepository: AuthenticationRepository,
    private val secrets: ISecrets,
    private val prefs: SharedPreferenceHelper
) {

    suspend fun refreshTokenAsNeeded(statusCode: StatusCode) {
        if (statusCode != StatusCode.Unauthorized) {
            return
        }

        val clientId = secrets.getClientId(BuildConfig.APPLICATION_ID).toInt()
        val clientSecret = secrets.getClientSecret(BuildConfig.APPLICATION_ID)

        if (prefs.isClientLogin()) {
            val clientTokenResponse = authRepository.getClientAccessToken(
                clientId, clientSecret, grantType = Constants.GRANT_TYPE_CLIENT
            )

           clientTokenResponse.onSuccess {
               Timber.d("Refreshed client token")
               prefs.saveClientAccessToken("${Constants.TOKEN_TYPE} ${data.accessToken}")
           }

        } else {
            val userTokenResponse = authRepository.refreshUserAccessToken(
                clientId = secrets.getClientId(BuildConfig.APPLICATION_ID).toInt(),
                clientSecret = secrets.getClientSecret(BuildConfig.APPLICATION_ID),
                refreshToken = prefs.getUserRefreshToken() ?: ""
            )

            userTokenResponse.onSuccess {
                Timber.d("Refreshed user token")
                prefs.saveUserAccessToken("${Constants.TOKEN_TYPE} ${data.accessToken}")
                prefs.saveUserRefreshToken(data.refreshToken)
            }
        }
    }
}