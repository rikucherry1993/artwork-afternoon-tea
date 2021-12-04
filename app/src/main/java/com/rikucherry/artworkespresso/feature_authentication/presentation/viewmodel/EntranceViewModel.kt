package com.rikucherry.artworkespresso.feature_authentication.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rikucherry.artworkespresso.common.tool.LocalResource
import com.rikucherry.artworkespresso.common.tool.SharedPreferenceHelper
import com.rikucherry.artworkespresso.common.tool.ViewModelState
import com.rikucherry.artworkespresso.feature_authentication.data.local.data_source.LoginStatus
import com.rikucherry.artworkespresso.feature_authentication.data.local.data_source.getStatus
import com.rikucherry.artworkespresso.feature_authentication.domain.use_case.GetLoginInfoUseCase
import com.rikucherry.artworkespresso.feature_authentication.domain.use_case.UserLoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class EntranceViewModel @Inject constructor(
    private val userLoginUseCase: UserLoginUseCase,
    private val getLoginInfoUseCase: GetLoginInfoUseCase,
    private val prefs: SharedPreferenceHelper,
) : ViewModel() {

    private val _state = mutableStateOf(ViewModelState<LoginStatus>())
    val state: State<ViewModelState<LoginStatus>> = _state

    fun formAuthorizeUri(state: String, isTopicEmpty: Boolean) =
        userLoginUseCase.formAuthorizeUri(state, isTopicEmpty)

    fun getUserTopics(): MutableSet<String>? = prefs.getUserFavoriteTopics()

    fun getClientTopics(): MutableSet<String>? = prefs.getClientFavoriteTopics()

    fun getLoginStatus() {
        getLoginInfoUseCase().onEach{ result ->
            when(result) {
              is LocalResource.Loading -> {
                  _state.value = ViewModelState(
                      isLoading = true
                  )
                  Timber.d(result.message)
              }

              is LocalResource.Success -> {
                  _state.value = ViewModelState(
                      isLoading = false,
                      data = result.data.getStatus()
                  )
                  Timber.d("Loading login info succeeded. \nData: ${result.data}")
              }

              is LocalResource.Fail -> {
                  _state.value = ViewModelState(
                      isLoading = false,
                      data = result.data?.getStatus(),
                      error = result.message
                  )
                  Timber.d("Loading login info failed. \nMessage: ${result.message}")
              }

              is LocalResource.Exception -> {
                  _state.value = ViewModelState(
                      isLoading = false,
                      error = result.message
                  )
                  Timber.d("Exception occurred. \nMessage: ${result.message}")
              }
            }
        }.launchIn(viewModelScope)
    }
}