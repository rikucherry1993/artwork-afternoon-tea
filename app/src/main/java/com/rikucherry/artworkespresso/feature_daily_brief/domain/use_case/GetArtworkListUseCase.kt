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
        offset: Int,
        weekday: String
    ): Flow<Resource<DeviationListDto>> = flow {
        emit(Resource.Loading<DeviationListDto>("Requesting daily artwork list..."))

        val artworkListResult = dailyBriefRepository.getArtworksByTopic(token, topic, 50, offset)

        artworkListResult.suspendOnSuccess {
            // Only the first 5 elements meeting filtering conditions are required
            val dataRequired = filter(data.results, weekday)
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

    private fun filter(list: List<DeviationDto>, weekday: String): List<DeviationDto> {
        val result: MutableList<DeviationDto> = mutableListOf()
        run loop@{
            list.filter { it.content?.src != null } // the ones that have contents
                // NOTE: Why I commented out the line below is because there wouldn't be enough data
                // to display if the list is filtered to only non-favourited items.
                // .filterNot { it.isFavourited == true } // the ones that haven't got a Like
                .filter {
                    // if the weekday is equal to the given weekday
                    DataFormatHelper.getWeekDayOfTimeStamp(it.publishedTime) == weekday
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