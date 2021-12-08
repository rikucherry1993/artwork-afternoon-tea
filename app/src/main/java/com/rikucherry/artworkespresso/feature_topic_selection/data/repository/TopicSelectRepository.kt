package com.rikucherry.artworkespresso.feature_topic_selection.data.repository

import com.rikucherry.artworkespresso.feature_topic_selection.data.remote.data_source.TopTopicListDto
import com.skydoves.sandwich.ApiResponse

interface TopicSelectRepository {

    suspend fun getTopTopics(token: String): ApiResponse<TopTopicListDto>

}