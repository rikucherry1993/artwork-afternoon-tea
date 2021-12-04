package com.rikucherry.artworkespresso.common.tool

import android.app.Application
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.message
import com.skydoves.sandwich.operators.ApiResponseSuspendOperator
import timber.log.Timber

class GlobalResponseOperator<T> constructor(
    private val application: Application
) : ApiResponseSuspendOperator<T>() {

    override suspend fun onError(apiResponse: ApiResponse.Failure.Error<T>) {
        Timber.e( "HTTP Error Code: ${apiResponse.statusCode.code} \n" +
                "Error Status: ${apiResponse.statusCode} \n" +
                "Message: ${apiResponse.message()}")
    }

    override suspend fun onException(apiResponse: ApiResponse.Failure.Exception<T>) {
        Timber.e("Exception occurred. \nMessage: ${apiResponse.message()}")
    }

    override suspend fun onSuccess(apiResponse: ApiResponse.Success<T>) {
        Timber.d("API calling succeeded. \nData: ${apiResponse.data}")
    }
}