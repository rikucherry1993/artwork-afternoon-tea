package com.rikucherry.artworkespresso.feature_authentication.data.local.data_source

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rikucherry.artworkespresso.common.data.local.ArtworkEspressoDatabase.Companion.LOGIN_TABLE_NAME

@Entity(tableName = LOGIN_TABLE_NAME)
data class LoginInfoItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "user_id")
    val userId:String,
    @ColumnInfo(name = "user_name")
    val userName: String,
    @ColumnInfo(name = "user_icon_url")
    val userIconUrl: String,
    @ColumnInfo(name = "status")
    val status: Int //1:logged out 2:user logged in 3: client logged in
)

fun LoginInfoItem.getStatus(): LoginStatus {
    return when(status) {
        1 -> LoginStatus.LOGGED_OUT
        2 -> LoginStatus.USER_LOGGED_IN
        3 -> LoginStatus.CLIENT_LOGGED_IN
        else -> LoginStatus.INVALID
    }
}

enum class LoginStatus {
    INVALID, LOGGED_OUT, USER_LOGGED_IN, CLIENT_LOGGED_IN
}
