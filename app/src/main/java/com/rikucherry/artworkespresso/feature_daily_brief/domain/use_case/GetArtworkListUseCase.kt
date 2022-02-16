package com.rikucherry.artworkespresso.feature_daily_brief.domain.use_case

import com.rikucherry.artworkespresso.common.data.remote.DeviationDto
import com.rikucherry.artworkespresso.common.data.remote.DeviationListDto
import com.rikucherry.artworkespresso.common.tool.DataFormatHelper
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

    operator fun invoke(
        token: String,
        topic: String,
        offset: Int
    ): Flow<Resource<DeviationListDto>> = flow {
        emit(Resource.Loading<DeviationListDto>("Requesting daily artwork list..."))

        val artworkListResult = dailyBriefRepository.getArtworksByTopic(token, topic, 50, offset)

        artworkListResult.suspendOnSuccess {
            // Only the first 5 elements meeting filtering conditions are required
            val dataRequired = filter(data.results)
            val hasMore = data.hasMore
            val nextOffset = data.nextOffset
            val filteredResult = DeviationListDto(
                hasMore = hasMore,
                results = dataRequired,
                nextOffset = nextOffset
            )
            emit(Resource.Success(filteredResult, statusCode))
        }.suspendOnError {
            emit(Resource.Error<DeviationListDto>(statusCode, toString()))
        }.suspendOnException {
            emit(Resource.Exception<DeviationListDto>(message ?: "Undefined exception."))
        }

    }

    private fun filter(list: List<DeviationDto>): List<DeviationDto> {
        val result: MutableList<DeviationDto> = mutableListOf()
        run loop@{
            list.filter { it.content?.src != null } // the ones that have contents
//                .filterNot { it.isFavourited == true } // the ones that haven't got a Like
                .filter {
                    // if the weekday is equal to the given weekday
                    // TODO: use passed weekday instead of today
                    DataFormatHelper.getWeekDayOfTimeStamp(it.publishedTime) == DataFormatHelper.getWeekdayOfToday()
                }
                .forEach { item ->
                    result.add(item)
                    if (result.size >= 5) {
                        return@loop
                    }
                }
        }
        return result
    }

}