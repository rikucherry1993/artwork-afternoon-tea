package com.rikucherry.artworkespresso.feature_authentication.domain.use_case

import android.net.Uri
import com.rikucherry.artworkespresso.BuildConfig
import com.rikucherry.artworkespresso.ISecrets
import com.rikucherry.artworkespresso.common.Constants
import com.rikucherry.artworkespresso.common.tool.Resource
import com.rikucherry.artworkespresso.feature_authentication.data.remote.data_source.toUserTokenResponse
import com.rikucherry.artworkespresso.feature_authentication.data.repository.AuthenticationRepository
import com.rikucherry.artworkespresso.feature_authentication.domain.model.UserTokenResponse
import com.rikucherry.artworkespresso.feature_authentication.domain.util.AuthenticationUtil
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserLoginUseCase @Inject constructor(
    private val authRepository: AuthenticationRepository,
    private val secrets: ISecrets
) {

    fun formAuthorizeUri(state: String, isTopicEmpty: Boolean): Uri {
        return AuthenticationUtil.formAuthorizeUri(
            responseType = Constants.AUTH_RESPONSE_TYPE,
            clientId = secrets.getClientId(BuildConfig.APPLICATION_ID),
            redirectUri = Constants.REDIRECT_URI_SCHEME
                    + if (isTopicEmpty) {
                Constants.REDIRECT_HOST_TOPIC
            } else {
                Constants.REDIRECT_HOST_DAILY
            },
            scope = Constants.FULL_SCOPE,
            state = state,
            view = Constants.AUTH_VIEW
        )
    }


    operator fun invoke(authCode: String, isTopicEmpty: Boolean):
            Flow<Resource<UserTokenResponse>> = flow {
        emit(Resource.Loading<UserTokenResponse>("Requesting access token..."))
        val userTokenResponse = authRepository.getUserAccessToken(
            clientId = secrets.getClientId(BuildConfig.APPLICATION_ID).toInt(),
            clientSecret = secrets.getClientSecret(BuildConfig.APPLICATION_ID),
            grantType = Constants.GRANT_TYPE_AUTH_CODE,
            code = authCode,
            redirectUri = Constants.REDIRECT_URI_SCHEME
                    + if (isTopicEmpty) {
                Constants.REDIRECT_HOST_TOPIC
            } else {
                Constants.REDIRECT_HOST_DAILY
            }
        )
        userTokenResponse.suspendOnSuccess {
            emit(Resource.Success(data.toUserTokenResponse(), statusCode, toString()))
        }.suspendOnError {
            emit(Resource.Error<UserTokenResponse>(statusCode, toString()))
        }.suspendOnException {
            emit(Resource.Exception<UserTokenResponse>(message ?: "Undefined exception."))
        }
    }
}