package com.rikucherry.artworkespresso.feature_daily_brief.domain.use_case

import com.rikucherry.artworkespresso.ISecrets
import com.rikucherry.artworkespresso.common.data.remote.DeviationDto
import com.rikucherry.artworkespresso.common.tool.BaseUseCase
import com.rikucherry.artworkespresso.common.tool.Resource
import com.rikucherry.artworkespresso.common.tool.SharedPreferenceHelper
import com.rikucherry.artworkespresso.feature_authentication.data.repository.AuthenticationRepository
import com.rikucherry.artworkespresso.feature_daily_brief.data.repository.DailyBriefRepository
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetArtworksByIdUseCase @Inject constructor(
    private val dailyBriefRepository: DailyBriefRepository,
    authRepository: AuthenticationRepository,
    secrets: ISecrets,
    prefs: SharedPreferenceHelper
): BaseUseCase(authRepository, secrets, prefs) {
    operator fun invoke(vararg ids: String, token: String): Flow<Resource<List<DeviationDto>>> = flow {
        emit(Resource.Loading("Requesting artwork by id..."))
        val artworks = mutableListOf<DeviationDto>()

        ids.forEach { id ->
            dailyBriefRepository.getArtworkById(token, id)
                .suspendOnSuccess {
                    artworks.add(data)
                    if (artworks.size == ids.size) {
                        emit(Resource.Success(artworks, statusCode))
                    }
                }
                .suspendOnError {
                    refreshTokenAsNeeded(statusCode)
                    emit(Resource.Error<List<DeviationDto>>(statusCode, toString()))
                }
                .suspendOnException {
                    emit(Resource.Exception<List<DeviationDto>>(message ?: "Undefined exception."))
                }
        }

    }
}