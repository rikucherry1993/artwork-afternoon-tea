package com.rikucherry.artworkespresso.common.data.remote


import com.google.gson.annotations.SerializedName

data class ContentDto(
    @SerializedName("filesize")
    val filesize: Int,
    @SerializedName("height")
    val height: Int,
    @SerializedName("src")
    val src: String,
    @SerializedName("transparency")
    val transparency: Boolean,
    @SerializedName("width")
    val width: Int
)