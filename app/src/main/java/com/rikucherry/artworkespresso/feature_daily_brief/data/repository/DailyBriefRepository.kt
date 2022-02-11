package com.rikucherry.artworkespresso.feature_daily_brief.data.repository

import com.rikucherry.artworkespresso.common.data.remote.DeviationListDto
import com.skydoves.sandwich.ApiResponse

interface DailyBriefRepository {

    suspend fun getArtworksByTopic(token: String, topic: String): ApiResponse<DeviationListDto>

    suspend fun getDailyArtworks(token: String, date: String?): ApiResponse<DeviationListDto>
}