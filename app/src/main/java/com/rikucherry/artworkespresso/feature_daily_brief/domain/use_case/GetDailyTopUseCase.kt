package com.rikucherry.artworkespresso.feature_daily_brief.domain.use_case

import com.rikucherry.artworkespresso.common.data.remote.DeviationDto
import com.rikucherry.artworkespresso.common.tool.DataFormatHelper
import com.rikucherry.artworkespresso.common.tool.Resource
import com.rikucherry.artworkespresso.feature_daily_brief.data.repository.DailyBriefRepository
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetDailyTopUseCase@Inject constructor(
    private val dailyBriefRepository: DailyBriefRepository
) {

    operator fun invoke(token: String, weekday: String): Flow<Resource<DeviationDto>> = flow {
        emit(Resource.Loading<DeviationDto>("Requesting data..."))

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
            emit(Resource.Error<DeviationDto>(statusCode, toString()))
        }.suspendOnException {
            emit(Resource.Exception<DeviationDto>(message ?: "Undefined exception."))
        }

    }
}