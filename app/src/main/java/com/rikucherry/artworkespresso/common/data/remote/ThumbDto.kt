package com.rikucherry.artworkespresso.common.data.remote


import com.google.gson.annotations.SerializedName

data class ThumbDto(
    @SerializedName("height")
    val height: Int,
    @SerializedName("src")
    val src: String,
    @SerializedName("transparency")
    val transparency: Boolean,
    @SerializedName("width")
    val width: Int
)