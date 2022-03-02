package com.rikucherry.artworkespresso.feature_daily_brief.data.repository

import com.rikucherry.artworkespresso.common.data.remote.DeviationDto
import com.rikucherry.artworkespresso.common.data.remote.DeviationListDto
import com.rikucherry.artworkespresso.feature_daily_brief.data.local.data_source.SavedArtworkItem
import com.skydoves.sandwich.ApiResponse

interface DailyBriefRepository {
    // API
    suspend fun getArtworksByTopic(token: String, topic: String, limit: Int?, offset: Int?): ApiResponse<DeviationListDto>

    suspend fun getDailyArtworks(token: String, date: String?): ApiResponse<DeviationListDto>

    suspend fun getArtworkById(token: String, deviationId: String): ApiResponse<DeviationDto>

    // DB transactions
    suspend fun saveArtworks(artworks: List<SavedArtworkItem>)

    suspend fun getArtworksByWeekday(weekday: String, isFreeTrail: Boolean): List<SavedArtworkItem>?

    suspend fun deleteSavedArtworksByWeekday(weekday: String, isFreeTrail: Boolean)
}