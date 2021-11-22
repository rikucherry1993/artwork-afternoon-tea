package com.rikucherry.artworkespresso.feature_authentication.domain.model

data class UserTokenResponse(
    val accessToken: String,
    val refreshToken: String,
)
