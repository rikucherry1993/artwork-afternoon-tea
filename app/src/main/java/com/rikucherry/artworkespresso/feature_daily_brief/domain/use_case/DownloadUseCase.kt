package com.rikucherry.artworkespresso.feature_daily_brief.domain.use_case

import com.rikucherry.artworkespresso.ISecrets
import com.rikucherry.artworkespresso.common.tool.BaseUseCase
import com.rikucherry.artworkespresso.common.tool.Resource
import com.rikucherry.artworkespresso.common.tool.SharedPreferenceHelper
import com.rikucherry.artworkespresso.feature_authentication.data.repository.AuthenticationRepository
import com.rikucherry.artworkespresso.feature_daily_brief.data.remote.data_source.DownloadDto
import com.rikucherry.artworkespresso.feature_daily_brief.data.repository.DailyBriefRepository
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DownloadUseCase @Inject constructor(
    private val dailyBriefRepository: DailyBriefRepository,
    authRepository: AuthenticationRepository,
    secrets: ISecrets,
    prefs: SharedPreferenceHelper
): BaseUseCase(authRepository, secrets, prefs) {

    operator fun invoke(token: String, deviationId: String): Flow<Resource<DownloadDto>> = flow {
        emit(Resource.Loading<DownloadDto>("Downloading artwork..."))

        val downloadResponse = dailyBriefRepository.getDownloadInfo(token, deviationId)

        downloadResponse.suspendOnSuccess {
            emit(Resource.Success<DownloadDto>(data, statusCode))
            dailyBriefRepository.downloadImage(data)
        }.suspendOnError {
            refreshTokenAsNeeded(statusCode)
            emit(Resource.Error<DownloadDto>(statusCode, toString()))
        }.suspendOnException {
            emit(Resource.Exception<DownloadDto>(message ?: "Undefined exception."))
        }
    }

}