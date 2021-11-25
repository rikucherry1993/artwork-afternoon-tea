package com.rikucherry.artworkespresso.common.tool

import android.app.Application
import android.util.Log
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.message
import com.skydoves.sandwich.operators.ApiResponseSuspendOperator

const val TAG = "GlobalResponseOperator"

class GlobalResponseOperator<T> constructor(
    private val application: Application
) : ApiResponseSuspendOperator<T>() {

    override suspend fun onError(apiResponse: ApiResponse.Failure.Error<T>) {
        Log.e(TAG, "HTTP Error Code: ${apiResponse.statusCode.code} \n" +
                "Error Status: ${apiResponse.statusCode} \n" +
                "Message: ${apiResponse.message()}")
    }

    override suspend fun onException(apiResponse: ApiResponse.Failure.Exception<T>) {
        Log.e(TAG, "Exception occurred. \nMessage: ${apiResponse.message()}")
    }

    override suspend fun onSuccess(apiResponse: ApiResponse.Success<T>) {
        Log.d(TAG, "API calling succeeded. \nData: ${apiResponse.data}")
    }
}