package com.rikucherry.artworkespresso.common.tool

import com.skydoves.sandwich.StatusCode

data class ViewModelState<T>(
    val isLoading: Boolean = false,
    val data: T? = null,
    val statusCode: Int? = null,
    val status: StatusCode? = null,
    val message: String? = null
)
