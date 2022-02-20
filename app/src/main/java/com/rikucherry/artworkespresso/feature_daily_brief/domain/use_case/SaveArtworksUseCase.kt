package com.rikucherry.artworkespresso.feature_daily_brief.domain.use_case

import com.rikucherry.artworkespresso.common.tool.LocalResource
import com.rikucherry.artworkespresso.feature_daily_brief.data.local.data_source.SavedArtworkItem
import com.rikucherry.artworkespresso.feature_daily_brief.data.repository.DailyBriefRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SaveArtworksUseCase @Inject constructor(
    private val dailyBriefRepository: DailyBriefRepository
) {

    operator fun invoke(artworks: List<SavedArtworkItem>): Flow<LocalResource<List<SavedArtworkItem>>> =
        flow {
            try {
                emit(LocalResource.Loading("Inserting artworks..."))

                dailyBriefRepository.saveArtworks(artworks)
                emit(LocalResource.Success(artworks))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(LocalResource.Exception(e.localizedMessage))
            }

        }
}