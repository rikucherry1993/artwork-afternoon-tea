package com.rikucherry.artworkespresso.feature_authentication.data.repository

import com.rikucherry.artworkespresso.common.data.remote.UserDto
import com.skydoves.sandwich.ApiResponse

interface UserRepository {

    suspend fun getWhoAmI(token: String): ApiResponse<UserDto>

}