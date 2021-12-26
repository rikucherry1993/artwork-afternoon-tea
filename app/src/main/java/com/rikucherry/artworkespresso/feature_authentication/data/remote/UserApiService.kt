package com.rikucherry.artworkespresso.feature_authentication.data.remote

import com.rikucherry.artworkespresso.common.data.remote.UserDto
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Header

interface UserApiService {

    /**
     * Request current user info
     * @see <a href="https://www.deviantart.com/developers/http/v1/20210526/user_whoami/2413749853e66c5812c9beccc0ab3495">documentation</a>
     */
    @GET(
        "user/whoami"
    )
    suspend fun getWhoAmI(@Header("Authorization") token: String): ApiResponse<UserDto>
}