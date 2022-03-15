package com.rikucherry.artworkespresso.feature_daily_brief.data.remote.data_source

import com.google.gson.annotations.SerializedName

data class FaveDto(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("favourites")
    val favourites: Int,
)
