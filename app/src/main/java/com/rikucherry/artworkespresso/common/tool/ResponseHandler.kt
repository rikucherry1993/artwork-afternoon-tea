package com.rikucherry.artworkespresso.common.tool

import com.skydoves.sandwich.StatusCode

sealed class ResponseHandler<out T>() {
    class Success<T>(val data: T, val statusCode: StatusCode, val message: String? = null) : ResponseHandler<T>()
    class Error<T>(val statusCode: StatusCode, val message: String) : ResponseHandler<T>()
    class Exception<T>(val message: String) : ResponseHandler<T>()
    class Loading<T>(val message:String? = null) : ResponseHandler<T>()
}
