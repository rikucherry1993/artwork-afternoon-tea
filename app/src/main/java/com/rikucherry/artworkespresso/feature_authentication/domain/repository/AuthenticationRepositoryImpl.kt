package com.rikucherry.artworkespresso.feature_authentication.domain.repository

import com.rikucherry.artworkespresso.feature_authentication.data.local.LoginInfoDao
import com.rikucherry.artworkespresso.feature_authentication.data.local.data_source.LoginInfoItem
import com.rikucherry.artworkespresso.feature_authentication.data.remote.AuthenticationApiService
import com.rikucherry.artworkespresso.feature_authentication.data.remote.data_source.ClientTokenResponseDto
import com.rikucherry.artworkespresso.feature_authentication.data.remote.data_source.UserTokenResponseDto
import com.rikucherry.artworkespresso.feature_authentication.data.repository.AuthenticationRepository
import com.skydoves.sandwich.ApiResponse
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val loginInfoDao: LoginInfoDao,
    private val authApi: AuthenticationApiService
) : AuthenticationRepository {

    override suspend fun getLoginInfo(): LoginInfoItem? {
        return loginInfoDao.getLoginInfo()
    }


    override suspend fun insertLoginInfo(loginInfoItem: LoginInfoItem) {
        return loginInfoDao.insertLoginInfo(loginInfoItem)
    }


    override suspend fun truncateLoginInfo() {
        return loginInfoDao.truncateLoginInfo()
    }


    override suspend fun getUserAccessToken(
        clientId: Int,
        clientSecret: String,
        grantType: String,
        code: String,
        redirectUri: String
    ): ApiResponse<UserTokenResponseDto> {
        return authApi.getUserAccessToken(clientId, clientSecret, grantType, code, redirectUri)
    }

    override suspend fun getClientAccessToken(
        clientId: Int,
        clientSecret: String,
        grantType: String
    ): ApiResponse<ClientTokenResponseDto> {
        return authApi.getClientAccessToken(clientId, clientSecret, grantType)
    }
}