package com.rikucherry.artworkespresso.common

object Constants {

    //API url
    const val BASE_URL = "https://www.deviantart.com"
    const val BASE_AUTH_PATH = "/oauth2/authorize"
    const val BASE_API_PATH = "/api/v1/oauth2"
    const val REDIRECT_URI = "com.rikucherry.artworkespresso.oauth://callback"
    //Auth state
    const val FULL_SCOPE = "browse browse.mlt collection user"
    const val DEFAULT_SCOPE = "browse"
    //Auth type
    const val AUTH_RESPONSE_TYPE = "code"
    const val AUTH_VIEW = "login"
    const val GRANT_TYPE_CLIENT = "client_credentials"
    const val GRANT_TYPE_AUTH_CODE = "authorization_code"
}