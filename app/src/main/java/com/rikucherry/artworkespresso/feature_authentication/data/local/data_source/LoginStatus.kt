package com.rikucherry.artworkespresso.feature_authentication.data.local.data_source

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LoginStatus(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val userId:String,
    val userName: String,
    val userIconUrl: String,
    val status: Int //1:logged out 2:user logged in 3: client logged in
)
