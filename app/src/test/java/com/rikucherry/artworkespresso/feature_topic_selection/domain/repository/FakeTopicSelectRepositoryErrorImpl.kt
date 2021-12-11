package com.rikucherry.artworkespresso.feature_topic_selection.domain.repository

import com.rikucherry.artworkespresso.feature_authentication.domain.repository.ErrorResponse
import com.rikucherry.artworkespresso.feature_topic_selection.data.remote.data_source.TopTopicListDto
import com.rikucherry.artworkespresso.feature_topic_selection.data.repository.TopicSelectRepository
import com.skydoves.sandwich.ApiResponse
import okhttp3.ResponseBody.Companion.toResponseBody

/**
 * Mock repository implementation of TopicSelectRepository
 *
 * For error responses
 */
class FakeTopicSelectRepositoryErrorImpl: TopicSelectRepository {

    /**
     * Mock up an error response from getTopTopics Api
     *
     * error code: 400
     */
    override suspend fun getTopTopics(token: String): ApiResponse<TopTopicListDto> {
        val errorResponse = ErrorResponse(
            error = "invalid_request",
            errorDescription = "fake message."
        )

        return ApiResponse.Failure.Error(retrofit2.Response
            .error(400, errorResponse.toString().toResponseBody()))
    }
}