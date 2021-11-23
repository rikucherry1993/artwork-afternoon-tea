package com.rikucherry.artworkespresso.feature_authentication.domain.use_case

import com.rikucherry.artworkespresso.BuildConfig
import com.rikucherry.artworkespresso.Secrets
import com.rikucherry.artworkespresso.common.Constants
import com.rikucherry.artworkespresso.common.tool.ResponseHandler
import com.rikucherry.artworkespresso.feature_authentication.data.remote.data_source.toClientTokenResponse
import com.rikucherry.artworkespresso.feature_authentication.data.repository.AuthenticationRepository
import com.rikucherry.artworkespresso.feature_authentication.domain.model.ClientTokenResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ClientLoginUseCase @Inject constructor(private val authRepository: AuthenticationRepository) {

    operator fun invoke(): Flow<ResponseHandler<ClientTokenResponse>> = flow {
        try {
            emit(ResponseHandler.Loading("Requesting access token..."))
            val clientTokenResponse = authRepository.getClientAccessToken(
                clientId = Secrets().getClientId(BuildConfig.APPLICATION_ID).toInt(),
                clientSecret = Secrets().getClientSecret(BuildConfig.APPLICATION_ID),
                grantType = Constants.GRANT_TYPE_CLIENT
            ).toClientTokenResponse()
            emit(
                ResponseHandler.Success<ClientTokenResponse>(
                    clientTokenResponse,
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