package com.rikucherry.artworkespresso.feature_daily_brief.domain.repository

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import com.rikucherry.artworkespresso.common.data.remote.DeviationDto
import com.rikucherry.artworkespresso.common.data.remote.DeviationListDto
import com.rikucherry.artworkespresso.feature_daily_brief.data.local.SavedArtworksDao
import com.rikucherry.artworkespresso.feature_daily_brief.data.local.data_source.SavedArtworkItem
import com.rikucherry.artworkespresso.feature_daily_brief.data.remote.DailyBriefApiService
import com.rikucherry.artworkespresso.feature_daily_brief.data.remote.data_source.DownloadDto
import com.rikucherry.artworkespresso.feature_daily_brief.data.repository.DailyBriefRepository
import com.skydoves.sandwich.ApiResponse
import java.io.File
import javax.inject.Inject

class DailyBriefRepositoryImpl @Inject constructor(
    private val savedArtworksDao: SavedArtworksDao,
    private val dailyBriefApi: DailyBriefApiService,
    private val context: Context
) : DailyBriefRepository {

    override suspend fun getArtworksByTopic(
        token: String,
        topic: String,
        limit: Int?,
        offset: Int?
    ): ApiResponse<DeviationListDto> {
        return dailyBriefApi.getArtworksByTopic(token, topic, limit, offset)
    }

    override suspend fun getDailyArtworks(
        token: String,
        date: String?
    ): ApiResponse<DeviationListDto> {
        return dailyBriefApi.getDailyArtworks(token, date)
    }

    override suspend fun getArtworksByWeekday(
        weekday: String,
        isFreeTrail: Boolean
    ): List<SavedArtworkItem>? {
        return savedArtworksDao.getArtworksByWeekday(weekday, isFreeTrail)
    }

    override suspend fun deleteSavedArtworksByWeekday(weekday: String, isFreeTrail: Boolean) =
        savedArtworksDao.deleteSavedArtworksByWeekday(weekday, isFreeTrail)

    override suspend fun getArtworkById(
        token: String,
        deviationId: String
    ): ApiResponse<DeviationDto> {
        return dailyBriefApi.getArtworkById(token, deviationId)
    }

    override suspend fun getDownloadInfo(
        token: String,
        deviationId: String
    ): ApiResponse<DownloadDto> {
        return dailyBriefApi.getDownloadInfo(token, deviationId)
    }

    override suspend fun saveArtworks(artworks: List<SavedArtworkItem>) {
        return savedArtworksDao.saveArtworks(*artworks.toTypedArray())
    }


    override fun downloadImage(data: DownloadDto) {
        val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val uri = Uri.parse(data.src)
        val fileSize = data.filesize / 1000
        val request = DownloadManager.Request(uri)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setTitle("Downloading ${data.filename}, Size: $fileSize KB")
            .setDestinationInExternalPublicDir(
                Environment.DIRECTORY_PICTURES,
                "${File.separator}ArtworkEspresso${File.separator}${System.currentTimeMillis()}_${data.filename}")
        manager.enqueue(request)
    }
}