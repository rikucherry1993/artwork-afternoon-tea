package com.rikucherry.artworkespresso.common.tool

sealed class LocalResource<out T> {
    class Success<T>(val data: T, val message: String? = null) : LocalResource<T>()
    class Fail<T>(val data: T? = null, val message: String) : LocalResource<T>()
    class Exception<T>(val message: String) : LocalResource<T>()
    class Loading<T>(val message:String? = null) : LocalResource<T>()
}
