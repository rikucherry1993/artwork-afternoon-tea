package com.rikucherry.artworkespresso.feature_daily_brief.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rikucherry.artworkespresso.common.data.remote.DeviationDto
import com.rikucherry.artworkespresso.common.tool.*
import com.rikucherry.artworkespresso.feature_daily_brief.data.local.data_source.SavedArtworkItem
import com.rikucherry.artworkespresso.feature_daily_brief.domain.use_case.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DailyBriefViewModel @Inject constructor(
    private val getArtworkListUseCase: GetArtworkListUseCase,
    private val getDailyTopUseCase: GetDailyTopUseCase,
    private val getArtworksByIdUseCase: GetArtworksByIdUseCase,
    private val getSavedArtworksUseCase: GetSavedArtworksUseCase,
    private val saveArtworksUseCase: SaveArtworksUseCase,
    private val prefs: SharedPreferenceHelper
) : ViewModel() {

    // States of API data
    private val _listState = mutableStateOf(ViewModelState<List<DeviationDto>>(isLoading = false))
    val listState: State<ViewModelState<List<DeviationDto>>> = _listState

    private val _topState = mutableStateOf(ViewModelState<DeviationDto>(isLoading = false))
    val topState: State<ViewModelState<DeviationDto>> = _topState

    // States of local db transitions
    private val _dbTransactionState = mutableStateOf(ViewModelState<List<SavedArtworkItem>>(isLoading = false))
    val dbTransactionState: State<ViewModelState<List<SavedArtworkItem>>> = _dbTransactionState

    private var token: String
    private var topic: String
    var selectedWeekday: String

    init {
        if (prefs.isClientLogin()) {
            token = prefs.getClientAccessToken() ?: ""
            topic = prefs.getClientFavoriteTopics()?.elementAt(0) ?: ""
        } else {
            token = prefs.getUserAccessToken() ?: ""
            topic = prefs.getUserFavoriteTopics()?.elementAt(0) ?: ""
        }

        // Show data of current date by default
        selectedWeekday = DataFormatHelper.getWeekdayOfToday()
        getArtworks()
    }

    fun getArtworks() {
        getSavedArtworksUseCase(selectedWeekday, prefs.isClientLogin()).onEach { result ->
            when(result) {
                is LocalResource.Loading -> {
                    _dbTransactionState.value = ViewModelState(
                        isLoading = true
                    )
                }

                is LocalResource.Success -> {
                    _dbTransactionState.value = ViewModelState(
                        isLoading = false
                    )
                    Timber.d("Loading artworks from db succeeded. \nData: ${result.data}")

                    // If has saved data, display the same artworks based on saved IDs.
                    // Otherwise, get artworks by topic.
                    if (result.data?.size ?: 0 > 0) {
                        getArtworksById(result.data!!)
                    } else {
                        getDailyTopArtwork(weekday = selectedWeekday)
                        getArtworkListByTopic(offset = 0, weekday = selectedWeekday)
                    }
                }

                is LocalResource.Exception -> {
                    _dbTransactionState.value = ViewModelState(
                        isLoading = false,
                        error = result.message
                    )
                    Timber.d("Exception occurred. \nMessage: ${result.message}")
                }
                else -> {}
            }
        }.launchIn(viewModelScope)
    }

    private fun getDailyTopArtwork(weekday: String) {
        getDailyTopUseCase(token,weekday).onEach { result ->
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
                    saveArtworks(listOf(result.data), isTopArt = true)
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

    private fun getArtworkListByTopic(offset: Int, weekday: String) {
        getArtworkListUseCase(token, topic, offset, weekday).onEach { result ->
            when(result) {
                is Resource.Loading -> {
                    _listState.value = ViewModelState(
                        isLoading = true
                    )
                }

                is Resource.Success -> {
                    val data = result.data
                    // expand max searching targets to 500
                    if (data.results.size < 5 && data.hasMore && data.nextOffset < 500) {
                        this.getArtworkListByTopic(data.nextOffset, weekday)
                    } else {
                        _listState.value = ViewModelState(
                            isLoading = false,
                            data = result.data.results,
                            statusCode = result.statusCode.code,
                            status = result.statusCode
                        )
                        saveArtworks(result.data.results, isTopArt = false)
                    }
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

    private fun saveArtworks(artworks: List<DeviationDto>, isTopArt: Boolean) {
        val saveItems = artworks.map {
            SavedArtworkItem(
                deviationId = it.deviationId,
                weekDay = selectedWeekday,
                isFreeTrail = prefs.isClientLogin(),
                isTopArt = isTopArt,
                savedTime = System.currentTimeMillis()
            )
        }
        saveArtworksUseCase(saveItems).onEach { result ->
            when(result) {
                is LocalResource.Loading -> {
                    _dbTransactionState.value = ViewModelState(
                        isLoading = true
                    )
                }

                is LocalResource.Success -> {
                    _dbTransactionState.value = ViewModelState(
                        isLoading = false
                    )
                    Timber.d("Saving artworks to db succeeded. \nData: ${result.data}")
                }

                is LocalResource.Exception -> {
                    _dbTransactionState.value = ViewModelState(
                        isLoading = false,
                        error = result.message
                    )
                    Timber.d("Exception occurred. \nMessage: ${result.message}")
                }
                else -> {}
            }
        }.launchIn(viewModelScope)
    }

    private fun getArtworksById(artworks: List<SavedArtworkItem>) {
        // get top art
        val topArtId = artworks.filter { artwork -> artwork.isTopArt }[0].deviationId
        getArtworksByIdUseCase(topArtId, token = token).onEach { result ->
            when(result) {
                is Resource.Loading -> {
                    _topState.value = ViewModelState(
                        isLoading = true
                    )
                }

                is Resource.Success -> {
                    _topState.value = ViewModelState(
                        isLoading = false,
                        data = result.data[0],
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

        // get artwork list
        val listIds = artworks.filterNot { artwork -> artwork.isTopArt }.map { it.deviationId }
        getArtworksByIdUseCase(*listIds.toTypedArray(), token = token).onEach { result ->
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