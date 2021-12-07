package com.rikucherry.artworkespresso.common.tool

import com.skydoves.sandwich.StatusCode

data class ViewModelState<T>(
    val isLoading: Boolean = true,
    val data: T? = null,
    val error: String = "",
    val statusCode: Int? = null,
    val status: StatusCode? = null,
)
