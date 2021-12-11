package com.rikucherry.artworkespresso.feature_topic_selection.domain.repository

import com.rikucherry.artworkespresso.feature_topic_selection.data.remote.data_source.TopTopicListDto
import com.rikucherry.artworkespresso.feature_topic_selection.data.repository.TopicSelectRepository
import com.skydoves.sandwich.ApiResponse
import java.io.IOException

/**
 * Mock repository implementation of TopicSelectRepository
 *
 * For exceptions
 */
class FakeTopicSelectRepositoryExceptionImpl: TopicSelectRepository {

    /**
     * Mock up an error response from getTopTopics Api
     *
     * Throws IOException
     */
    override suspend fun getTopTopics(token: String): ApiResponse<TopTopicListDto> {
        val exception = IOException("Throw new IO exception")
        return ApiResponse.Failure.Exception(exception)
    }
}