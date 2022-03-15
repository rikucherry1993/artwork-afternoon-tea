package com.rikucherry.artworkespresso.feature_authentication.data.remote

import com.rikucherry.artworkespresso.feature_authentication.data.remote.data_source.ClientTokenResponseDto
import com.rikucherry.artworkespresso.feature_authentication.data.remote.data_source.UserTokenResponseDto
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthenticationApiService {

    /**
     * Request an access token using authorization code
     * @see <a href="https://www.deviantart.com/developers/authentication">documentation</a>
     */
    @FormUrlEncoded
    @POST(
        "/oauth2/token"
    )
    suspend fun getUserAccessToken(
        @Field("client_id") clientId: Int,
        @Field("client_secret") clientSecret: String,
        @Field("grant_type") grantType: String,
        @Field("code") code: String,
        @Field("redirect_uri") redirectUri: String,
    ): ApiResponse<UserTokenResponseDto>


    @FormUrlEncoded
    @POST("/oauth2/token")
    suspend fun getClientAccessToken(
        @Field("client_id") clientId: Int,
        @Field("client_secret") clientSecret: String,
        @Field("grant_type") grantType: String
    ): ApiResponse<ClientTokenResponseDto>


    @FormUrlEncoded
    @POST("/oauth2/token")
    suspend fun refreshUserAccessToken(
        @Field("client_id") clientId: Int,
        @Field("client_secret") clientSecret: String,
        @Field("grant_type") grantType: String,
        @Field("refresh_token") refreshToken: String
    ): ApiResponse<UserTokenResponseDto>
}