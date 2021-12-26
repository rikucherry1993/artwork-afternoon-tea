package com.rikucherry.artworkespresso.feature_authentication.domain.repository

import com.rikucherry.artworkespresso.common.data.remote.UserDto
import com.rikucherry.artworkespresso.feature_authentication.data.remote.UserApiService
import com.rikucherry.artworkespresso.feature_authentication.data.repository.UserRepository
import com.skydoves.sandwich.ApiResponse
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApiService
) : UserRepository {

    override suspend fun getWhoAmI(token: String): ApiResponse<UserDto> {
        return userApi.getWhoAmI(token)
    }
}