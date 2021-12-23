package com.rikucherry.artworkespresso.feature_topic_selection.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rikucherry.artworkespresso.common.tool.Resource
import com.rikucherry.artworkespresso.common.tool.SharedPreferenceHelper
import com.rikucherry.artworkespresso.common.tool.ViewModelState
import com.rikucherry.artworkespresso.feature_topic_selection.data.remote.data_source.TopTopicsDto
import com.rikucherry.artworkespresso.feature_topic_selection.domain.use_case.TopicSelectUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class TopicSelectViewModel @Inject constructor(
    private val topicSelectUseCase: TopicSelectUseCase,
    private val prefs: SharedPreferenceHelper
) : ViewModel() {

    private val _state = mutableStateOf(ViewModelState<List<TopTopicsDto>>())
    val state: State<ViewModelState<List<TopTopicsDto>>> = _state

    private var isClient = false
    private var token = ""

    init {
        getTopTopics()
    }

    private fun getTopTopics() {
        val userToken = prefs.getUserAccessToken()
        val clientToken = prefs.getClientAccessToken()

       if (userToken.isNullOrEmpty()) {
            token = clientToken ?: ""
            isClient = true
        } else {
            token = userToken
            isClient = false
        }

        topicSelectUseCase(token).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _state.value = ViewModelState(
                        isLoading = true
                    )
                }

                is Resource.Success -> {
                    _state.value = ViewModelState(
                        isLoading = false,
                        data = result.data,
                        statusCode = result.statusCode.code,
                        status = result.statusCode
                    )
                }

                is Resource.Error -> {
                    _state.value = ViewModelState(
                        isLoading = false,
                        statusCode = result.statusCode.code,
                        status = result.statusCode,
                        error = result.message
                    )
                }

                is Resource.Exception -> {
                    _state.value = ViewModelState(
                        isLoading = false,
                        error = result.message
                    )
                }
            }
        }.launchIn(viewModelScope)
    }


    fun saveFavouriteTopic (topicName : String) {
        if (isClient) {
            prefs.saveClientFavoriteTopics(mutableSetOf(topicName))
        } else {
            prefs.saveUserFavoriteTopics(mutableSetOf(topicName))
        }
    }

}