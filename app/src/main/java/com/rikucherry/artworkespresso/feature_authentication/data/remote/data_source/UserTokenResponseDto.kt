package com.rikucherry.artworkespresso.feature_authentication.data.remote.data_source


import com.google.gson.annotations.SerializedName

data class UserTokenResponseDto(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("expires_in")
    val expiresIn: Int,
    @SerializedName("refresh_token")
    val refreshToken: String,
    @SerializedName("scope")
    val scope: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("token_type")
    val tokenType: String
)