package com.rikucherry.artworkespresso.common.tool

import com.skydoves.sandwich.StatusCode

data class ViewModelState<T>(
    val isLoading: Boolean = false,
    val data: T? = null,
    val error: String? = null,
    val statusCode: Int? = null,
    val status: StatusCode? = null,
)
