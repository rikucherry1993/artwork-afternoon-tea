package com.rikucherry.artworkespresso.feature_authentication.view

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rikucherry.artworkespresso.common.ResponseHandler
import com.rikucherry.artworkespresso.feature_authentication.domain.use_case.UserLoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CallbackActivityViewModel @Inject constructor(
    private val userLoginUseCase: UserLoginUseCase
) : ViewModel() {

    private val _state = mutableStateOf("")
    val state: State<String> = _state

    fun getAccessToken(authCode: String?) {
        userLoginUseCase(authCode ?: "").onEach { result ->
            when (result) {
                is ResponseHandler.Success -> {
                    _state.value = result.data.toString()
                    Log.d("Auth state:", _state.value)
                }

                is ResponseHandler.Loading -> {
                    _state.value = result.message ?: ""
                    Log.d("Auth state:", _state.value)
                }

                is ResponseHandler.Error -> {
                    _state.value = result.message!!
                    Log.d("Auth state:", _state.value)
                }
            }
        }.launchIn(viewModelScope)
    }

}