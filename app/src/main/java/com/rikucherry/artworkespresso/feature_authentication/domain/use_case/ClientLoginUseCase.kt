package com.rikucherry.artworkespresso.feature_authentication.domain.use_case

import com.rikucherry.artworkespresso.BuildConfig
import com.rikucherry.artworkespresso.Secrets
import com.rikucherry.artworkespresso.common.Constants
import com.rikucherry.artworkespresso.common.tool.ResponseHandler
import com.rikucherry.artworkespresso.feature_authentication.data.remote.data_source.toClientTokenResponse
import com.rikucherry.artworkespresso.feature_authentication.data.repository.AuthenticationRepository
import com.rikucherry.artworkespresso.feature_authentication.domain.model.ClientTokenResponse
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ClientLoginUseCase @Inject constructor(private val authRepository: AuthenticationRepository) {

    operator fun invoke(): Flow<ResponseHandler<ClientTokenResponse>> = flow {
        emit(ResponseHandler.Loading<ClientTokenResponse>("Requesting access token..."))
        val clientTokenResponse = authRepository.getClientAccessToken(
            clientId = Secrets().getClientId(BuildConfig.APPLICATION_ID).toInt(),
            clientSecret = Secrets().getClientSecret(BuildConfig.APPLICATION_ID),
            grantType = Constants.GRANT_TYPE_CLIENT
        )
        clientTokenResponse.suspendOnSuccess {
            emit(ResponseHandler.Success<ClientTokenResponse>(data.toClientTokenResponse(), statusCode, toString()))
        }.suspendOnError {
            emit(ResponseHandler.Error<ClientTokenResponse>(statusCode, toString()))
        }.suspendOnException {
            emit(ResponseHandler.Exception<ClientTokenResponse>(message ?: "Undefined exception. "))
        }

    }

}