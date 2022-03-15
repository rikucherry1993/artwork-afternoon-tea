package com.rikucherry.artworkespresso.feature_daily_brief.data.remote

import com.rikucherry.artworkespresso.common.data.remote.DeviationDto
import com.rikucherry.artworkespresso.common.data.remote.DeviationListDto
import com.rikucherry.artworkespresso.feature_daily_brief.data.remote.data_source.DownloadDto
import com.rikucherry.artworkespresso.feature_daily_brief.data.remote.data_source.FaveDto
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.*

interface DailyBriefApiService {

    /**
     * Request artworks by topic name
     * @see <a href="https://www.deviantart.com/developers/http/v1/20210526/browse_tags/804dfdfd8e831a0b136f636b5eabd788">documentation</a>
     */
    @GET(
        "browse/tags"
    )
    suspend fun getArtworksByTopic(@Header("Authorization") token: String
                                   , @Query("tag") topic: String
                                   , @Query("limit") limit: Int?
                                   , @Query("offset") offset: Int?): ApiResponse<DeviationListDto>


    /**
     * Request artworks by date (default is today)
     * @see <a href="https://www.deviantart.com/developers/http/v1/20210526/browse_topic/2a61608c6f8dfb32f8e12372080cfb34">documentation</a>
     */
    @GET(
        "browse/dailydeviations"
    )
    suspend fun getDailyArtworks(@Header("Authorization") token: String
                                 , @Query("date") date: String?): ApiResponse<DeviationListDto>


    @GET(
        "deviation/{deviationid}"
    )
    suspend fun getArtworkById(@Header("Authorization") token: String
                               , @Path("deviationid") deviationId: String): ApiResponse<DeviationDto>


    @GET(
        "deviation/download/{deviationid}"
    )
    suspend fun getDownloadInfo(@Header("Authorization") token: String
                                , @Path("deviationid") deviationId: String): ApiResponse<DownloadDto>

    @FormUrlEncoded
    @POST(
        "collections/fave"
    )
    suspend fun faveArtById(@Header("Authorization") token: String
                            , @Field("deviationid") deviationId: String
                            , @Field("folderid") folderIds: List<String>?): ApiResponse<FaveDto>

    @FormUrlEncoded
    @POST(
        "collections/unfave"
    )
    suspend fun unfaveArtById(@Header("Authorization") token: String
                            , @Field("deviationid") deviationId: String
                            , @Field("folderid") folderIds: List<String>?): ApiResponse<FaveDto>

}