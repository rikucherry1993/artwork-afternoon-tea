package com.rikucherry.artworkespresso.feature_topic_selection.domain.repository

import com.rikucherry.artworkespresso.feature_topic_selection.data.remote.TopicSelectApiService
import com.rikucherry.artworkespresso.feature_topic_selection.data.remote.data_source.TopTopicsDto
import com.rikucherry.artworkespresso.feature_topic_selection.data.repository.TopicSelectRepository
import com.skydoves.sandwich.ApiResponse
import javax.inject.Inject

class TopicSelectRepositoryImpl @Inject constructor(
    private val topicSelectApi: TopicSelectApiService
) : TopicSelectRepository {

    override suspend fun getTopTopics(token: String): ApiResponse<List<TopTopicsDto>> {
        return topicSelectApi.getTopTopics(token)
    }

}