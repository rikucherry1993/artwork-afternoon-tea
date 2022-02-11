package com.rikucherry.artworkespresso.feature_daily_brief.domain.use_case

import com.rikucherry.artworkespresso.common.data.remote.DeviationDto
import com.rikucherry.artworkespresso.common.tool.Resource
import com.rikucherry.artworkespresso.feature_daily_brief.data.repository.DailyBriefRepository
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetArtworkListUseCase @Inject constructor(
    private val dailyBriefRepository: DailyBriefRepository
) {

    operator fun invoke(token: String, topic: String): Flow<Resource<List<DeviationDto>>> = flow {
        emit(Resource.Loading<List<DeviationDto>>("Requesting data..."))

        val artworkListResult = dailyBriefRepository.getArtworksByTopic(token, topic)

        artworkListResult.suspendOnSuccess {
            // Only the first ten elements are required
            // TODO: Implement filtering logic
            val dataRequired = if (data.results.size > 10) {
                data.results.subList(0, 10)
            } else {
                data.results
            }
            emit(Resource.Success(dataRequired, statusCode))
        }.suspendOnError {
            emit(Resource.Error<List<DeviationDto>>(statusCode, toString()))
        }.suspendOnException {
            emit(Resource.Exception<List<DeviationDto>>(message ?: "Undefined exception."))
        }

    }
    
}