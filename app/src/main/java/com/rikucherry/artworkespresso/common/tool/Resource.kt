package com.rikucherry.artworkespresso.common.tool

import com.skydoves.sandwich.StatusCode

sealed class Resource<out T> {
    class Success<T>(val data: T, val statusCode: StatusCode, val message: String? = null) : Resource<T>()
    class Error<T>(val statusCode: StatusCode, val message: String) : Resource<T>()
    class Exception<T>(val message: String) : Resource<T>()
    class Loading<T>(val message:String? = null) : Resource<T>()
}
