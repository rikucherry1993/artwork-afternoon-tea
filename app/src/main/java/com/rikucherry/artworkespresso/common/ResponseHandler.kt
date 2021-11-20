package com.rikucherry.artworkespresso.common

sealed class ResponseHandler<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T, message: String?) : ResponseHandler<T>(data, message)
    class Error<T>(message: String, data: T? = null) : ResponseHandler<T>(data, message)
    class Loading<T>(message: String?, data: T? = null) : ResponseHandler<T>(data, message)
}
