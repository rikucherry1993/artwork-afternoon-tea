package com.rikucherry.artworkespresso.feature_authentication.data.remote.data_source


import com.google.gson.annotations.SerializedName
import com.rikucherry.artworkespresso.feature_authentication.domain.model.ClientTokenResponse

data class ClientTokenResponseDto(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("expires_in")
    val expiresIn: Int,
    @SerializedName("status")
    val status: String,
    @SerializedName("token_type")
    val tokenType: String
)

fun ClientTokenResponseDto.toClientTokenResponse() : ClientTokenResponse {
    return ClientTokenResponse(accessToken = accessToken)
}