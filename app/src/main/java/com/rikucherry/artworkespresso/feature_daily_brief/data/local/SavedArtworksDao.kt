package com.rikucherry.artworkespresso.feature_daily_brief.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rikucherry.artworkespresso.feature_daily_brief.data.local.data_source.SavedArtworkItem

@Dao
interface SavedArtworksDao {

    @Query("SELECT * FROM saved_artworks WHERE week_day = :weekday AND is_free_trail = :isFreeTrail")
    suspend fun getArtworksByWeekday(weekday: String, isFreeTrail: Boolean): List<SavedArtworkItem>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveArtworks(vararg artworks: SavedArtworkItem)

    @Query("DELETE FROM saved_artworks WHERE week_day = :weekday AND is_free_trail = :isFreeTrail")
    suspend fun deleteSavedArtworksByWeekday(weekday: String, isFreeTrail: Boolean)

}