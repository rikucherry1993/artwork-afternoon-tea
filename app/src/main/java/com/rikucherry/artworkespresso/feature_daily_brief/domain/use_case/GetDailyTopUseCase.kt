package com.rikucherry.artworkespresso.feature_daily_brief.domain.use_case

import com.rikucherry.artworkespresso.ISecrets
import com.rikucherry.artworkespresso.common.data.remote.DeviationDto
import com.rikucherry.artworkespresso.common.tool.BaseUseCase
import com.rikucherry.artworkespresso.common.tool.DataFormatHelper
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

class GetDailyTopUseCase @Inject constructor(
    private val dailyBriefRepository: DailyBriefRepository,
    authRepository: AuthenticationRepository,
    secrets: ISecrets,
    prefs: SharedPreferenceHelper
): BaseUseCase(authRepository,secrets, prefs) {

    operator fun invoke(token: String, weekday: String): Flow<Resource<DeviationDto>> = flow {
        emit(Resource.Loading<DeviationDto>("Requesting daily top artwork..."))

        val date = if (weekday == DataFormatHelper.getWeekdayOfToday()) {
            null
        } else {
            DataFormatHelper.getDateFromWeekday(weekday)
        }

        val artworkListResult = dailyBriefRepository.getDailyArtworks(token, date)
        artworkListResult.suspendOnSuccess {
            // Only the first element is required
            val dataRequired = data.results[0]
            emit(Resource.Success<DeviationDto>(dataRequired, statusCode))
        }.suspendOnError {
            refreshTokenAsNeeded(statusCode)
            emit(Resource.Error<DeviationDto>(statusCode, toString()))
        }.suspendOnException {
            emit(Resource.Exception<DeviationDto>(message ?: "Undefined exception."))
        }

    }
}