package com.rikucherry.artworkespresso.feature_topic_selection.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rikucherry.artworkespresso.common.tool.LocalResource
import com.rikucherry.artworkespresso.common.tool.Resource
import com.rikucherry.artworkespresso.common.tool.SharedPreferenceHelper
import com.rikucherry.artworkespresso.common.tool.ViewModelState
import com.rikucherry.artworkespresso.feature_authentication.data.local.data_source.LoginInfoItem
import com.rikucherry.artworkespresso.feature_authentication.domain.use_case.GetUserUseCase
import com.rikucherry.artworkespresso.feature_authentication.domain.use_case.InsertLoginInfoUseCase
import com.rikucherry.artworkespresso.feature_topic_selection.data.remote.data_source.TopTopicsDto
import com.rikucherry.artworkespresso.feature_topic_selection.domain.use_case.TopicSelectUseCase
import com.skydoves.sandwich.StatusCode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TopicSelectViewModel @Inject constructor(
    private val topicSelectUseCase: TopicSelectUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val insertLoginInfoUseCase: InsertLoginInfoUseCase,
    private val prefs: SharedPreferenceHelper
) : ViewModel() {

    private val _topicState = mutableStateOf(ViewModelState<List<TopTopicsDto>>())
    val topicState: State<ViewModelState<List<TopTopicsDto>>> = _topicState

    private val _userState = mutableStateOf(ViewModelState<LoginInfoItem>())
    val userState: State<ViewModelState<LoginInfoItem>> = _userState

    private val _loginInfoState = mutableStateOf(ViewModelState<LoginInfoItem>(isLoading = false))
    val loginInfoState: State<ViewModelState<LoginInfoItem>> = _loginInfoState

    init {
        getTopTopics()
        setLoginInfoByUser()
    }

    private fun getTopTopics() {
        val token = if (prefs.isClientLogin()) {
            prefs.getClientAccessToken()
        } else {
            prefs.getUserAccessToken()
        } ?: ""

        topicSelectUseCase(token).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _topicState.value = ViewModelState(
                        isLoading = true
                    )
                }

                is Resource.Success -> {
                    _topicState.value = ViewModelState(
                        isLoading = false,
                        data = result.data,
                        statusCode = result.statusCode.code,
                        status = result.statusCode
                    )
                }

                is Resource.Error -> {
                    _topicState.value = ViewModelState(
                        isLoading = false,
                        statusCode = result.statusCode.code,
                        status = result.statusCode,
                        error = result.message
                    )
                }

                is Resource.Exception -> {
                    _topicState.value = ViewModelState(
                        isLoading = false,
                        error = result.message
                    )
                }
            }
        }.launchIn(viewModelScope)
    }


    fun saveFavouriteTopic (raw : String) {
        val topicName = raw.replace("\\s+".toRegex(), "").lowercase()
        if (prefs.isClientLogin()) {
            prefs.saveClientFavoriteTopics(mutableSetOf(topicName))
            Timber.d("Saving client favorite topic: $topicName")
        } else {
            prefs.saveUserFavoriteTopics(mutableSetOf(topicName))
            Timber.d("Saving user favorite topic: $topicName")
        }
    }


    private fun setLoginInfoByUser () {
        if (prefs.isClientLogin()) {
            _userState.value = ViewModelState(
                isLoading = false,
                data = LoginInfoItem(1, "", "", "", 3),
                statusCode = StatusCode.OK.code,
                status = StatusCode.OK
            )
        } else {
            getUserUseCase(prefs.getUserAccessToken() ?: "").onEach { result ->
                when(result) {
                    is Resource.Loading -> {
                        _userState.value = ViewModelState(
                            isLoading = true
                        )
                    }

                    is Resource.Success -> {
                        _userState.value = ViewModelState(
                            isLoading = false,
                            data = LoginInfoItem(
                                id = 1,
                                userId = result.data.userid,
                                userName = result.data.username,
                                userIconUrl = result.data.userIconUrl,
                                status = 2
                            ),
                            statusCode = result.statusCode.code,
                            status = result.statusCode
                        )
                    }

                    is Resource.Error -> {
                        _userState.value = ViewModelState(
                            isLoading = false,
                            statusCode = result.statusCode.code,
                            status = result.statusCode,
                            error = result.message
                        )
                    }

                    is Resource.Exception -> {
                        _userState.value = ViewModelState(
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
            }.launchIn(viewModelScope)

        }
    }


    fun insertLoginInfo() {
        val loginInfo = userState.value.data!!
        insertLoginInfoUseCase(loginInfo).onEach { result ->
            when (result) {
                is LocalResource.Loading -> {
                    _loginInfoState.value = ViewModelState(
                        isLoading = true,
                    )
                    Timber.d(result.message)
                }

                is LocalResource.Success -> {
                    _loginInfoState.value = ViewModelState(
                        isLoading = false,
                        data = result.data,
                        status = StatusCode.OK
                    )

                    Timber.d("Inserting login info succeeded. \nData: ${result.data}")
                }

                is LocalResource.Exception -> {
                    _loginInfoState.value = ViewModelState(
                        isLoading = false,
                        error = result.message
                    )
                    Timber.d("Exception occurred. \nMessage: ${result.message}")
                }

                else -> {}
            }
        }.launchIn(viewModelScope)

    }

}