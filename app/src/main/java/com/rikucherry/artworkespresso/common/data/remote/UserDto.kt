package com.rikucherry.artworkespresso.common.data.remote


import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("type")
    val type: String,
    @SerializedName("usericon")
    val userIconUrl: String,
    @SerializedName("userid")
    val userid: String,
    @SerializedName("username")
    val username: String
)