package com.rikucherry.artworkespresso.feature_authentication.data.local.data_source

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "login_info")
data class LoginInfoItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val userId:String,
    val userName: String,
    val userIconUrl: String,
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
