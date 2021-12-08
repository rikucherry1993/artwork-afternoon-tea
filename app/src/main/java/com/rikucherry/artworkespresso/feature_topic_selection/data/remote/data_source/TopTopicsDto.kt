package com.rikucherry.artworkespresso.feature_topic_selection.data.remote.data_source


import com.google.gson.annotations.SerializedName
import com.rikucherry.artworkespresso.common.data.remote.DeviationDto

data class TopTopicsDto(
    @SerializedName("canonical_name")
    val canonicalName: String,
    @SerializedName("example_deviations")
    val exampleDeviations: List<DeviationDto>? = null,
    @SerializedName("name")
    val name: String
)

data class TopTopicListDto(
    @SerializedName("results")
    val results: List<TopTopicsDto>
)