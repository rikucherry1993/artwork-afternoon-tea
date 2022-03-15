package com.rikucherry.artworkespresso.common.tool

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import com.skydoves.sandwich.StatusCode

data class ViewModelState<T>(
    val isLoading: Boolean = true,
    val data: T? = null,
    val error: String = "",
    val statusCode: Int? = null,
    val status: StatusCode? = null,
)

fun <T,R> ViewModel.updateState(
    result: Resource<T>,
    state: MutableState<ViewModelState<R>>,
    successData:R?
) {
    when (result) {
        is Resource.Loading -> {
            state.value = ViewModelState(
                isLoading = true
            )
        }

        is Resource.Success -> {
            state.value = ViewModelState(
                isLoading = false,
                data = successData,
                statusCode = result.statusCode.code,
                status = result.statusCode
            )
        }

        is Resource.Error -> {
            state.value = ViewModelState(
                isLoading = false,
                statusCode = result.statusCode.code,
                status = result.statusCode,
                error = result.message
            )
        }

        is Resource.Exception -> {
            state.value = ViewModelState(
                isLoading = false,
                error = result.message
            )
        }
    }
}
