package com.rikucherry.artworkespresso.feature_authentication.domain.use_case

import android.net.Uri
import com.rikucherry.artworkespresso.BuildConfig
import com.rikucherry.artworkespresso.Secrets
import com.rikucherry.artworkespresso.common.Constants
import com.rikucherry.artworkespresso.common.tool.ResponseHandler
import com.rikucherry.artworkespresso.feature_authentication.data.remote.data_source.toUserTokenResponse
import com.rikucherry.artworkespresso.feature_authentication.data.repository.AuthenticationRepository
import com.rikucherry.artworkespresso.feature_authentication.domain.model.UserTokenResponse
import com.rikucherry.artworkespresso.feature_authentication.domain.util.AuthenticationUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserLoginUseCase @Inject constructor(private val authRepository: AuthenticationRepository) {

    companion object {
        fun formAuthorizeUri(state: String): Uri {
            return AuthenticationUtil.formAuthorizeUri(
                responseType = Constants.AUTH_RESPONSE_TYPE,
                clientId = Secrets().getClientId(BuildConfig.APPLICATION_ID),
                redirectUri = Constants.REDIRECT_URI,
                scope = Constants.FULL_SCOPE,
                state = state,
                view = Constants.AUTH_VIEW
            )
        }
    }

    operator fun invoke(authCode: String):
            Flow<ResponseHandler<UserTokenResponse>> = flow {
        try {
            emit(ResponseHandler.Loading("Requesting access token..."))
            val userTokenResponse = authRepository.getUserAccessToken(
                clientId = Secrets().getClientId(BuildConfig.APPLICATION_ID).toInt(),
                clientSecret = Secrets().getClientSecret(BuildConfig.APPLICATION_ID),
                grantType = Constants.GRANT_TYPE_AUTH_CODE,
                code = authCode,
                redirectUri = Constants.REDIRECT_URI
            ).toUserTokenResponse()
            emit(
                ResponseHandler.Success<UserTokenResponse>(
                    userTokenResponse,
                    "Authentication succeeded."
                )
            )
        } catch (e: Exception) {
            emit(
                ResponseHandler.Error(
                    "Failed to get access token: ${e.localizedMessage ?: "Undefined cause."}"
                )
            )
        }
    }
}