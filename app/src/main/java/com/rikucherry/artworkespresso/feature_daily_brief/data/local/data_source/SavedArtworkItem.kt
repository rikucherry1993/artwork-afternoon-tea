package com.rikucherry.artworkespresso.feature_daily_brief.data.local.data_source

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rikucherry.artworkespresso.common.data.local.ArtworkEspressoDatabase.Companion.SAVED_ARTWORK_TABLE_NAME

@Entity(tableName = SAVED_ARTWORK_TABLE_NAME)
data class SavedArtworkItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "deviation_id")
    val deviationId: String,
    @ColumnInfo(name = "week_day")
    val weekDay: String,
    @ColumnInfo(name = "is_free_trail")
    val isFreeTrail: Boolean,
    @ColumnInfo(name = "is_top_art")
    val isTopArt: Boolean,
)
