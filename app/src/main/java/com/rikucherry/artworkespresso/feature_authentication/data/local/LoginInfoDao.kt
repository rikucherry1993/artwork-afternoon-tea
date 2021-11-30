package com.rikucherry.artworkespresso.feature_authentication.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rikucherry.artworkespresso.feature_authentication.data.local.data_source.LoginInfoItem

@Dao
interface LoginInfoDao {

    @Query("SELECT * FROM login_info LIMIT 1")
    suspend fun getLoginInfo(): LoginInfoItem

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLogInfo(loginInfoItem: LoginInfoItem)

    @Query("DELETE FROM login_info")
    suspend fun truncateLoginIngo()

}