package com.rikucherry.artworkespresso.feature_authentication.view

import android.content.Intent
import android.os.Bundle
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rikucherry.artworkespresso.common.Constants
import com.rikucherry.artworkespresso.common.tool.ResponseHandler
import com.rikucherry.artworkespresso.common.tool.SharedPreferenceHelper
import com.rikucherry.artworkespresso.feature_authentication.domain.use_case.UserLoginUseCase
import com.rikucherry.artworkespresso.feature_authentication.domain.util.AuthenticationUtil
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class LoginViewModel @AssistedInject constructor(
    private val userLoginUseCase: UserLoginUseCase,
    private val prefs: SharedPreferenceHelper,
    @Assisted private val args: Bundle?
) : ViewModel() {

    private val _state = mutableStateOf("")
    val state: State<String> = _state

    init {
        val intent = args?.getParcelable<Intent>(Constants.AUTH_INTENT)
        val state = args?.getString(Constants.AUTH_STATE)
        val isTopicEmpty = args?.getBoolean(Constants.IS_TOPIC_EMPTY)
        getAccessToken(intent, state ?: "", isTopicEmpty ?: false)
    }

    private fun getAccessToken(intent: Intent?, state: String, isTopicEmpty: Boolean) {
        val authCode = AuthenticationUtil.retrieveAuthorizeCode(intent, state)

        userLoginUseCase(authCode ?: "", isTopicEmpty).onEach { result ->
            when (result) {
                is ResponseHandler.Success -> {
                    _state.value = result.data.toString()
                    prefs.saveUserAccessToken(result.data!!.accessToken)
                    prefs.saveUserRefreshToken(result.data!!.refreshToken)
                }

                is ResponseHandler.Loading -> {
                    _state.value = result.message ?: ""
                }

                is ResponseHandler.Error -> {
                    _state.value = result.message!!
                }
            }
        }.launchIn(viewModelScope)
    }

}