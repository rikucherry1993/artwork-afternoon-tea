package com.rikucherry.artworkespresso.feature_daily_brief.domain.repository

import com.rikucherry.artworkespresso.common.data.remote.DeviationDto
import com.rikucherry.artworkespresso.common.data.remote.DeviationListDto
import com.rikucherry.artworkespresso.feature_daily_brief.data.local.SavedArtworksDao
import com.rikucherry.artworkespresso.feature_daily_brief.data.local.data_source.SavedArtworkItem
import com.rikucherry.artworkespresso.feature_daily_brief.data.remote.DailyBriefApiService
import com.rikucherry.artworkespresso.feature_daily_brief.data.repository.DailyBriefRepository
import com.skydoves.sandwich.ApiResponse
import javax.inject.Inject

class DailyBriefRepositoryImpl @Inject constructor(
    private val savedArtworksDao: SavedArtworksDao,
    private val dailyBriefApi: DailyBriefApiService
) : DailyBriefRepository{

    override suspend fun getArtworksByTopic(
        token: String,
        topic: String,
        limit: Int?,
        offset: Int?
    ): ApiResponse<DeviationListDto> {
        return dailyBriefApi.getArtworksByTopic(token, topic, limit, offset)
    }

    override suspend fun getDailyArtworks(token: String, date: String?): ApiResponse<DeviationListDto> {
        return dailyBriefApi.getDailyArtworks(token, date)
    }

    override suspend fun getArtworksByWeekday(weekday: String, isFreeTrail: Boolean): List<SavedArtworkItem>? {
        return savedArtworksDao.getArtworksByWeekday(weekday, isFreeTrail)
    }

    override suspend fun deleteSavedArtworksByWeekday(weekday: String) = savedArtworksDao.deleteSavedArtworksByWeekday(weekday)

    override suspend fun getArtworkById(token: String, deviationId: String): ApiResponse<DeviationDto> {
        return dailyBriefApi.getArtworkById(token, deviationId)
    }

    override suspend fun saveArtworks(artworks: List<SavedArtworkItem>) {
        return savedArtworksDao.saveArtworks(*artworks.toTypedArray())
    }
}