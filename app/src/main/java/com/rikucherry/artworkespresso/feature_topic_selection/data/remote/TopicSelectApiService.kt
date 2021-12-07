package com.rikucherry.artworkespresso.feature_topic_selection.data.remote

import com.rikucherry.artworkespresso.feature_topic_selection.data.remote.data_source.TopTopicsDto
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Header

interface TopicSelectApiService {

    /**
     * Request top topics
     * @see <a href="https://www.deviantart.com/developers/http/v1/20210526/browse_toptopics/54fceab4f37618344f35849568a2b29e">documentation</a>
     */
    @GET(
        "/browse/toptopics"
    )
    suspend fun getTopTopics(@Header("Authorization") token: String): ApiResponse<List<TopTopicsDto>>
}