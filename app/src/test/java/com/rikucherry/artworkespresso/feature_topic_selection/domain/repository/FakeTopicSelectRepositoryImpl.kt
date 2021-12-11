package com.rikucherry.artworkespresso.feature_topic_selection.domain.repository

import com.rikucherry.artworkespresso.common.data.remote.ContentDto
import com.rikucherry.artworkespresso.common.data.remote.DeviationDto
import com.rikucherry.artworkespresso.feature_topic_selection.data.remote.data_source.TopTopicListDto
import com.rikucherry.artworkespresso.feature_topic_selection.data.remote.data_source.TopTopicsDto
import com.rikucherry.artworkespresso.feature_topic_selection.data.repository.TopicSelectRepository
import com.skydoves.sandwich.ApiResponse

/**
 * Mock repository implementation of TopicSelectRepository
 *
 * For successful responses
 */
class FakeTopicSelectRepositoryImpl: TopicSelectRepository {

    val topicNoExample = TopTopicsDto(
        canonicalName = "canonical name 1",
        name = "name 1",
    )

    val topicHasExamples = TopTopicsDto(
        canonicalName = "canonicalName name 2",
        name = "name 2",
        exampleDeviations = listOf<DeviationDto>(DeviationDto(
            content = ContentDto(
                filesize = 500000,
                height = 1048,
                src = "https://test.jpg",
                transparency = false,
                width = 1860,
            ),
            isDeleted = false,
            deviationId = "fake id"
        ))
    )

    override suspend fun getTopTopics(token: String): ApiResponse<TopTopicListDto> {

        val topTopicListDto = TopTopicListDto(
            results = listOf(topicNoExample,topicHasExamples)
        )

        return ApiResponse.Success(retrofit2.Response.success(topTopicListDto))
    }
}