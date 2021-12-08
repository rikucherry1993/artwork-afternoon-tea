package com.rikucherry.artworkespresso.common.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rikucherry.artworkespresso.feature_authentication.data.local.LoginInfoDao
import com.rikucherry.artworkespresso.feature_authentication.data.local.data_source.LoginInfoItem

@Database(entities = [LoginInfoItem::class], version = 1, exportSchema = true)
abstract class ArtworkEspressoDatabase : RoomDatabase() {

    abstract fun loginInfoDao(): LoginInfoDao

    companion object {
        const val DATABASE_NAME = "artwork_espresso_db"
    }
}