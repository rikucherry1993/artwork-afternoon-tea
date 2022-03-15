package com.rikucherry.artworkespresso.feature_daily_brief.domain.use_case

import com.rikucherry.artworkespresso.ISecrets
import com.rikucherry.artworkespresso.common.tool.BaseUseCase
import com.rikucherry.artworkespresso.common.tool.Resource
import com.rikucherry.artworkespresso.common.tool.SharedPreferenceHelper
import com.rikucherry.artworkespresso.feature_authentication.data.repository.AuthenticationRepository
import com.rikucherry.artworkespresso.feature_daily_brief.data.remote.data_source.FaveDto
import com.rikucherry.artworkespresso.feature_daily_brief.data.repository.DailyBriefRepository
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FaveOrUnfaveArtworkUseCase @Inject constructor(
    private val dailyBriefRepository: DailyBriefRepository,
    authRepository: AuthenticationRepository,
    secrets: ISecrets,
    prefs: SharedPreferenceHelper
): BaseUseCase(authRepository, secrets, prefs) {

    operator fun invoke(token: String, doFave: Boolean, deviationId: String): Flow<Resource<FaveDto>> = flow {
        val response = if (doFave) {
            emit(Resource.Loading<FaveDto>("Performing setting $deviationId to favourite..."))
            dailyBriefRepository.faveArtById(token, deviationId, null)
        } else {
            emit(Resource.Loading<FaveDto>("Performing setting $deviationId to unfavourite..."))
            dailyBriefRepository.unfaveArtById(token, deviationId, null)
        }
        response.suspendOnSuccess {
            emit(Resource.Success<FaveDto>(data, statusCode))
        }.suspendOnError {
            refreshTokenAsNeeded(statusCode)
            emit(Resource.Error<FaveDto>(statusCode, toString()))
        }.suspendOnException {
            emit(Resource.Exception<FaveDto>(message ?: "Undefined exception."))
        }
    }

}