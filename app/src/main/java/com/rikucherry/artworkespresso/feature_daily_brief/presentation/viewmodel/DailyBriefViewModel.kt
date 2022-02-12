package com.rikucherry.artworkespresso.feature_daily_brief.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rikucherry.artworkespresso.common.data.remote.DeviationDto
import com.rikucherry.artworkespresso.common.tool.Resource
import com.rikucherry.artworkespresso.common.tool.SharedPreferenceHelper
import com.rikucherry.artworkespresso.common.tool.ViewModelState
import com.rikucherry.artworkespresso.feature_daily_brief.domain.use_case.GetArtworkListUseCase
import com.rikucherry.artworkespresso.feature_daily_brief.domain.use_case.GetDailyTopUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class DailyBriefViewModel @Inject constructor(
    private val getArtworkListUseCase: GetArtworkListUseCase,
    private val getDailyTopUseCase: GetDailyTopUseCase,
    private val prefs: SharedPreferenceHelper
) : ViewModel() {

    private val _listState = mutableStateOf(ViewModelState<List<DeviationDto>>())
    val listState: State<ViewModelState<List<DeviationDto>>> = _listState

    private val _topState = mutableStateOf(ViewModelState<DeviationDto>())
    val topState: State<ViewModelState<DeviationDto>> = _topState

    private var token: String
    private var topic: String


    init {
        if (prefs.isClientLogin()) {
            token = prefs.getClientAccessToken() ?: ""
            topic = prefs.getClientFavoriteTopics()?.elementAt(0) ?: ""
        } else {
            token = prefs.getUserAccessToken() ?: ""
            topic = prefs.getUserFavoriteTopics()?.elementAt(0) ?: ""
        }
        getDailyTopArtwork()
        getArtworkListByTopic()
    }

    private fun getDailyTopArtwork() {
        getDailyTopUseCase(token,null).onEach { result ->
            when(result) {
                is Resource.Loading -> {
                    _topState.value = ViewModelState(
                        isLoading = true
                    )
                }

                is Resource.Success -> {
                    _topState.value = ViewModelState(
                        isLoading = false,
                        data = result.data,
                        statusCode = result.statusCode.code,
                        status = result.statusCode
                    )
                }

                is Resource.Error -> {
                    _topState.value = ViewModelState(
                        isLoading = false,
                        statusCode = result.statusCode.code,
                        status = result.statusCode,
                        error = result.message
                    )
                }

                is Resource.Exception -> {
                    _topState.value = ViewModelState(
                        isLoading = false,
                        error = result.message
                    )
                }
            }

        }.launchIn(viewModelScope)
    }



    private fun getArtworkListByTopic() {
        getArtworkListUseCase(token, topic).onEach { result ->
            when(result) {
                is Resource.Loading -> {
                    _listState.value = ViewModelState(
                        isLoading = true
                    )
                }

                is Resource.Success -> {
                    _listState.value = ViewModelState(
                        isLoading = false,
                        data = result.data,
                        statusCode = result.statusCode.code,
                        status = result.statusCode
                    )
                }

                is Resource.Error -> {
                    _listState.value = ViewModelState(
                        isLoading = false,
                        statusCode = result.statusCode.code,
                        status = result.statusCode,
                        error = result.message
                    )
                }

                is Resource.Exception -> {
                    _listState.value = ViewModelState(
                        isLoading = false,
                        error = result.message
                    )
                }
            }

        }.launchIn(viewModelScope)
    }

}