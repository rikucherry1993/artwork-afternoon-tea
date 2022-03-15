package com.rikucherry.artworkespresso.feature_daily_brief.data.remote.data_source

import com.google.gson.annotations.SerializedName

data class DownloadDto(
    @SerializedName("filesize")
    val filesize: Int,
    @SerializedName("filename")
    val filename: String,
    @SerializedName("height")
    val height: Int,
    @SerializedName("src")
    val src: String,
    @SerializedName("width")
    val width: Int
)
