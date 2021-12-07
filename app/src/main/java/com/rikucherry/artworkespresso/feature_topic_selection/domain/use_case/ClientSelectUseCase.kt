package com.rikucherry.artworkespresso.feature_topic_selection.domain.use_case

import com.rikucherry.artworkespresso.common.tool.Resource
import com.rikucherry.artworkespresso.feature_topic_selection.data.remote.data_source.TopTopicsDto
import com.rikucherry.artworkespresso.feature_topic_selection.data.repository.TopicSelectRepository
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ClientSelectUseCase @Inject constructor(
    private val topicSelectRepository: TopicSelectRepository
) {

    operator fun invoke(token: String): Flow<Resource<List<TopTopicsDto>>> = flow {
        emit(Resource.Loading<List<TopTopicsDto>>("Requesting data..."))
        val topTopicsResponse = topicSelectRepository.getTopTopics(token)

        topTopicsResponse.suspendOnSuccess {
            emit(Resource.Success<List<TopTopicsDto>>(data, statusCode))
        }.suspendOnError {
            emit(Resource.Error<List<TopTopicsDto>>(statusCode, toString()))
        }.suspendOnException {
            emit(Resource.Exception<List<TopTopicsDto>>(message ?: "Undefined exception."))
        }

    }
}