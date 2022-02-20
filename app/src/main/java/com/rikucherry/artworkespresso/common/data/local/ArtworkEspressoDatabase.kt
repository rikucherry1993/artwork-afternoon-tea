package com.rikucherry.artworkespresso.common.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rikucherry.artworkespresso.feature_authentication.data.local.LoginInfoDao
import com.rikucherry.artworkespresso.feature_authentication.data.local.data_source.LoginInfoItem
import com.rikucherry.artworkespresso.feature_daily_brief.data.local.SavedArtworksDao
import com.rikucherry.artworkespresso.feature_daily_brief.data.local.data_source.SavedArtworkItem

@Database(
    entities = [LoginInfoItem::class, SavedArtworkItem::class], version = 1, exportSchema = true
)
abstract class ArtworkEspressoDatabase : RoomDatabase() {

    abstract fun loginInfoDao(): LoginInfoDao
    abstract fun savedArtworks(): SavedArtworksDao

    companion object {
        const val DATABASE_NAME = "artwork_espresso_db"
        const val LOGIN_TABLE_NAME = "login_info"
        const val SAVED_ARTWORK_TABLE_NAME = "saved_artworks"
    }
}