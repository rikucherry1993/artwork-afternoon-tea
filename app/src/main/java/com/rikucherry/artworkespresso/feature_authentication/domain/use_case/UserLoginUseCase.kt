package com.rikucherry.artworkespresso.feature_authentication.domain.use_case

import android.content.Intent
import android.net.Uri
import com.rikucherry.artworkespresso.BuildConfig
import com.rikucherry.artworkespresso.Secrets
import com.rikucherry.artworkespresso.common.Constants
import com.rikucherry.artworkespresso.common.ResponseHandler
import com.rikucherry.artworkespresso.feature_authentication.data.remote.data_source.toUserTokenResponse
import com.rikucherry.artworkespresso.feature_authentication.data.repository.AuthenticationRepository
import com.rikucherry.artworkespresso.feature_authentication.domain.model.UserTokenResponse
import com.rikucherry.artworkespresso.feature_authentication.domain.util.AuthenticationUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserLoginUseCase @Inject constructor(private val authRepository: AuthenticationRepository){

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

    operator fun invoke(intent: Intent? = null, state: String): Flow<ResponseHandler<UserTokenResponse>> = flow {
        emit(ResponseHandler.Loading())
        val uri = intent?.data
        if (intent?.action != Intent.ACTION_VIEW || uri == null) {
            emit(ResponseHandler.Error("Authorization Failed."))
        } else {
            when {
                uri.getQueryParameter("state") != state -> {
                    emit(ResponseHandler.Error("Non-authorized state."))
                }
                uri.getQueryParameter("code") == null -> {
                    emit(ResponseHandler.Error("Authorization Failed."))
                }
                else -> {
                    try{
                        val userTokenResponse = authRepository.getUserAccessToken(
                            clientId = Secrets().getClientId(BuildConfig.APPLICATION_ID).toInt(),
                            clientSecret = Secrets().getClientSecret(BuildConfig.APPLICATION_ID),
                            grantType = Constants.GRANT_TYPE_AUTH_CODE,
                            code = uri.getQueryParameter("code")!!,
                            redirectUri = Constants.REDIRECT_URI
                            ).toUserTokenResponse()
                        emit(ResponseHandler.Success<UserTokenResponse>(userTokenResponse, "Authorized."))
                    } catch (e: Exception) {
                        emit(ResponseHandler.Error("Failed to get access token: ${e.localizedMessage ?: "Undefined cause."}" ))
                    }
                }
            }
        }
    }
}