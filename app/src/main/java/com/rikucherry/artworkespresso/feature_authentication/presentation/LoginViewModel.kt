package com.rikucherry.artworkespresso.feature_authentication.presentation

import android.content.Intent
import android.os.Bundle
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rikucherry.artworkespresso.common.Constants
import com.rikucherry.artworkespresso.common.tool.Resource
import com.rikucherry.artworkespresso.common.tool.SharedPreferenceHelper
import com.rikucherry.artworkespresso.common.tool.ViewModelState
import com.rikucherry.artworkespresso.feature_authentication.domain.use_case.ClientLoginUseCase
import com.rikucherry.artworkespresso.feature_authentication.domain.use_case.UserLoginUseCase
import com.rikucherry.artworkespresso.feature_authentication.domain.util.AuthenticationUtil
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class LoginViewModel @AssistedInject constructor(
    private val userLoginUseCase: UserLoginUseCase,
    private val clientLoginUseCase: ClientLoginUseCase,
    private val prefs: SharedPreferenceHelper,
    @Assisted private val args: Bundle?
) : ViewModel() {

    private val _state = mutableStateOf(ViewModelState<String>())
    val state: State<ViewModelState<String>> = _state

    init {
        val isFreeTrail = args?.getBoolean(Constants.IS_FREE_TRAIL) ?: false
        if (isFreeTrail) {
            getClientAccessToken()
        } else {
            val intent = args?.getParcelable<Intent>(Constants.AUTH_INTENT)
            val state = args?.getString(Constants.AUTH_STATE)
            val isTopicEmpty = args?.getBoolean(Constants.IS_TOPIC_EMPTY)
            getUserAccessToken(intent, state ?: "", isTopicEmpty ?: false)
        }
    }

    private fun getUserAccessToken(intent: Intent?, state: String, isTopicEmpty: Boolean) {
        val authCode = AuthenticationUtil.retrieveAuthorizeCode(intent, state)

        userLoginUseCase(authCode ?: "", isTopicEmpty).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = ViewModelState(
                        isLoading = false,
                        data = result.data.accessToken,
                        statusCode = result.statusCode.code,
                        status = result.statusCode,
                        message = result.data.accessToken
                    )
                    prefs.clearPrefs()
                    prefs.saveUserAccessToken(result.data!!.accessToken)
                    prefs.saveUserRefreshToken(result.data!!.refreshToken)
                }

                is Resource.Loading -> {
                    _state.value = ViewModelState(
                        isLoading = true,
                        message = result.message
                    )
                }

                is Resource.Error -> {
                    _state.value = ViewModelState(
                        isLoading = false,
                        statusCode = result.statusCode.code,
                        status = result.statusCode,
                        message = result.message
                    )
                }
                is Resource.Exception -> {
                    _state.value = ViewModelState(
                        isLoading = false,
                        message = result.message
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getClientAccessToken() {
        clientLoginUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = ViewModelState(
                        isLoading = false,
                        data = result.data.accessToken,
                        statusCode = result.statusCode.code,
                        status = result.statusCode,
                        message = result.data.accessToken
                    )
                    prefs.clearPrefs()
                    prefs.saveClientAccessToken(result.data.accessToken)
                }

                is Resource.Loading -> {
                    _state.value = ViewModelState(
                        isLoading = true,
                        message = result.message
                    )
                }

                is Resource.Error -> {
                    _state.value = ViewModelState(
                        isLoading = false,
                        statusCode = result.statusCode.code,
                        status = result.statusCode,
                        message = result.message
                    )
                }

                is Resource.Exception -> {
                    _state.value = ViewModelState(
                        isLoading = false,
                        message = result.message
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

}