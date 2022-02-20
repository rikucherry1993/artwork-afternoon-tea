package com.rikucherry.artworkespresso.common.data.remote


import com.google.gson.annotations.SerializedName

data class DeviationDto(
    @SerializedName("allows_comments")
    val allowsComments: Boolean? = null,
    @SerializedName("author")
    val author: UserDto? = null,
    @SerializedName("category")
    val category: String? = null,
    @SerializedName("category_path")
    val categoryPath: String? = null,
    @SerializedName("content")
    val content: ContentDto? = null,
    @SerializedName("deviationid")
    val deviationId: String,
    @SerializedName("download_filesize")
    val downloadFileSize: Int? = null,
    @SerializedName("is_deleted")
    val isDeleted: Boolean,
    @SerializedName("is_downloadable")
    val isDownloadable: Boolean? = null,
    @SerializedName("is_favourited")
    val isFavourited: Boolean? = null,
    @SerializedName("is_mature")
    val isMature: Boolean? = null,
    @SerializedName("is_published")
    val isPublished: Boolean? = null,
//    @SerializedName("preview")
//    val preview: Preview? = null,
    @SerializedName("printid")
    val printId: String? = null,
    @SerializedName("published_time")
    val publishedTime: String? = null,
//    @SerializedName("stats")
//    val stats: Stats? = null,
    @SerializedName("thumbs")
    val thumbs: List<ThumbDto>? = null,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("url")
    val url: String? = null,
//    @SerializedName("tier")
//    val tier: Tier? = null,
//    @SerializedName("videos")
//    val videos: List<Video>? = null,
//    @SerializedName("flash")
//    val flash: Flash? = null,
//    @SerializedName("daily_deviation")
//    val dailyDeviation: DailyDeviation? = null,
//    @SerializedName("premium_folder_data")
//    val premiumFolderData: PremiumFolderData? = null,
//    @SerializedName("text_content")
//    val textContent: TextContent? = null,
    @SerializedName("is_pinned")
    val isPinned: Boolean? = null,
//    @SerializedName("cover_image")
//    val coverImage: Deviation? = null,
//    @SerializedName("tier_access")
//    val tierAccess: String? = null,
//    @SerializedName("primary_tier")
//    val primaryTier: Deviation? = null,
//    @SerializedName("excerpt")
//    val excerpt: String? = null,
//    @SerializedName("motion_book")
//    val motionBook: MotionBook? = null,
//    @SerializedName("suggested_reasons")
//    val suggestedReasons: List<String>? = null
)

data class DeviationListDto(
    @SerializedName("has_more")
    val hasMore: Boolean,
    @SerializedName("next_offset")
    val nextOffset: Int,
    @SerializedName("results")
    val results: List<DeviationDto>
)