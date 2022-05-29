package com.rikucherry.artworkespresso.feature_daily_brief.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rikucherry.artworkespresso.common.data.remote.DeviationDto
import com.rikucherry.artworkespresso.common.tool.*
import com.rikucherry.artworkespresso.feature_authentication.data.local.data_source.LoginInfoItem
import com.rikucherry.artworkespresso.feature_authentication.domain.use_case.GetLoginInfoUseCase
import com.rikucherry.artworkespresso.feature_daily_brief.data.local.data_source.SavedArtworkItem
import com.rikucherry.artworkespresso.feature_daily_brief.data.remote.data_source.DownloadDto
import com.rikucherry.artworkespresso.feature_daily_brief.data.remote.data_source.FaveDto
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
    private val faveOrUnfaveArtworkUseCase: FaveOrUnfaveArtworkUseCase,
    private val saveArtworksUseCase: SaveArtworksUseCase,
    private val getLoginInfoUseCase: GetLoginInfoUseCase,
    private val downloadUseCase: DownloadUseCase,
    private val prefs: SharedPreferenceHelper
) : ViewModel() {

    // States of API data
    private val _listState = mutableStateOf(ViewModelState<List<DeviationDto>>(isLoading = false))
    val listState: State<ViewModelState<List<DeviationDto>>> = _listState

    private val _topState = mutableStateOf(ViewModelState<DeviationDto>(isLoading = false))
    val topState: State<ViewModelState<DeviationDto>> = _topState

    private val _faveState = mutableStateOf(ViewModelState<FaveDto>(isLoading = false))
    val faveState: State<ViewModelState<FaveDto>> = _faveState

    // States of local db transitions
    private val _savedItemState = mutableStateOf(ViewModelState<List<SavedArtworkItem>>(isLoading = false))
    val savedItemState: State<ViewModelState<List<SavedArtworkItem>>> = _savedItemState

    private val _downloadItemState = mutableStateOf(ViewModelState<DownloadDto>(isLoading = false))
    val downloadItemState: State<ViewModelState<DownloadDto>> = _downloadItemState

    private val _loginInfoState = mutableStateOf(ViewModelState<LoginInfoItem>(isLoading = false))
    val loginInfoState: State<ViewModelState<LoginInfoItem>> = _loginInfoState

    private lateinit var token: String
    private lateinit var topic: String
    var selectedWeekday: String

    init {
        setTokenAndTopic()
        // Show data of current date by default
        selectedWeekday = DataFormatHelper.getWeekdayOfToday()
        getArtworks()
        getLoginInfo()
    }

    fun getArtworks() {
        getSavedArtworksUseCase(selectedWeekday, prefs.isClientLogin()).onEach { result ->
            when(result) {
                is LocalResource.Loading -> {
                    _savedItemState.value = ViewModelState(
                        isLoading = true
                    )
                }

                is LocalResource.Success -> {
                    _savedItemState.value = ViewModelState(
                        isLoading = false
                    )
                    Timber.d("Loading artworks from db succeeded. \nData: ${result.data}")

                    // If has saved data, display the same artworks based on saved IDs.
                    // Otherwise, get artworks by topic.
                    if ((result.data?.size ?: 0) > 0) {
                        getTopArtworkById(result.data!!)
                        getArtworkListById(result.data)
                    } else {
                        getDailyTopArtwork(weekday = selectedWeekday)
                        getArtworkListByTopic(offset = 0, weekday = selectedWeekday)
                    }
                }

                is LocalResource.Exception -> {
                    _savedItemState.value = ViewModelState(
                        isLoading = false,
                        error = result.message
                    )
                    Timber.d("Exception occurred. \nMessage: ${result.message}")
                }
                else -> {}
            }
        }.launchIn(viewModelScope)
    }

    private fun getDailyTopArtwork(weekday: String, retry: Boolean = true) {
        getDailyTopUseCase(token,weekday).onEach { result ->
            when(result) {
                is Resource.Loading -> {
                    updateState(result, state = _topState, successData = null)
                }

                is Resource.Success -> {
                    updateState(result, state = _topState, successData = result.data)
                    saveArtworks(listOf(result.data), isTopArt = true)
                }

                else -> {
                    if (retry) {
                        setTokenAndTopic()
                        getDailyTopArtwork(weekday, false)
                    } else {
                        updateState(result, state = _topState, successData = null)
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getArtworkListByTopic(offset: Int, weekday: String, retry: Boolean = true) {
        getArtworkListUseCase(token, topic, offset, weekday).onEach { result ->
            when(result) {
                is Resource.Loading -> {
                    updateState(result, state = _listState, successData = null)
                }

                is Resource.Success -> {
                    val data = result.data
                    // expand max searching targets to 500
                    if (data.results.size < 5 && data.hasMore && data.nextOffset < 500) {
                        this.getArtworkListByTopic(data.nextOffset, weekday)
                    } else {
                        updateState(result, state = _listState, successData = result.data.results)
                        saveArtworks(result.data.results, isTopArt = false)
                    }
                }

                else -> {
                    if (retry) {
                        setTokenAndTopic()
                        getArtworkListByTopic(offset, weekday, false)
                    } else {
                        updateState(result, state = _listState, successData = null)
                    }
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
                    _savedItemState.value = ViewModelState(
                        isLoading = true
                    )
                }

                is LocalResource.Success -> {
                    _savedItemState.value = ViewModelState(
                        isLoading = false
                    )
                    Timber.d("Saving artworks to db succeeded. \nData: ${result.data}")
                }

                is LocalResource.Exception -> {
                    _savedItemState.value = ViewModelState(
                        isLoading = false,
                        error = result.message
                    )
                    Timber.d("Exception occurred. \nMessage: ${result.message}")
                }
                else -> {}
            }
        }.launchIn(viewModelScope)
    }

    private fun getTopArtworkById(artworks: List<SavedArtworkItem>, retry: Boolean = true) {
        // get top art
        val topArtId = artworks.filter { artwork -> artwork.isTopArt }[0].deviationId
        getArtworksByIdUseCase(topArtId, token = token).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    updateState(result, state = _topState, successData = null)
                }

                is Resource.Success -> {
                    updateState(result, state = _topState, successData = result.data[0])
                }

                else -> {
                    if (retry) {
                        setTokenAndTopic()
                        getTopArtworkById(artworks, false)
                    } else {
                        updateState(result, state = _topState, successData = null)
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getArtworkListById(artworks: List<SavedArtworkItem>, retry: Boolean = true) {
        // get artwork list
        val listIds = artworks.filterNot { artwork -> artwork.isTopArt }.map { it.deviationId }
        getArtworksByIdUseCase(*listIds.toTypedArray(), token = token).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    updateState(result, state = _listState, successData = null)
                }

                is Resource.Success -> {
                    updateState(result, state = _listState, successData = result.data)
                }

                else -> {
                    if (retry) {
                        setTokenAndTopic()
                        getArtworkListById(artworks, false)
                    } else {
                        updateState(result, state = _listState, successData = null)
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun faveOrUnFaveArtworkById(doFave: Boolean, artworkId: String, retry: Boolean = true) {
        faveOrUnfaveArtworkUseCase(token, doFave, artworkId).onEach { result ->
            when(result) {
                is Resource.Loading -> {
                    updateState(result, state = _faveState, successData = null)
                }

                is Resource.Success -> {
                    updateState(result, state = _faveState, successData = result.data)
                }

                else -> {
                    if (retry) {
                        setTokenAndTopic()
                        faveOrUnFaveArtworkById(doFave, artworkId, false)
                    } else {
                        updateState(result, state = _faveState, successData = null)
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getLoginInfo() {
        getLoginInfoUseCase().onEach { result ->
            when(result) {
                is LocalResource.Loading -> {
                    _loginInfoState.value = ViewModelState(
                        isLoading = true
                    )
                }

                is LocalResource.Success -> {
                    _loginInfoState.value = ViewModelState(
                        isLoading = false,
                        data = result.data
                    )
                    Timber.d("Getting login info from db succeeded. \nData: ${result.data}")
                }

                is LocalResource.Exception -> {
                    _loginInfoState.value = ViewModelState(
                        isLoading = false,
                        error = result.message
                    )
                    Timber.d("Exception occurred. \nMessage: ${result.message}")
                }

                is LocalResource.Fail -> {
                    _loginInfoState.value = ViewModelState(
                        isLoading = false,
                        error = result.message,
                        data = result.data
                    )
                    Timber.d("Loading login info failed. \nMessage: ${result.message}")
                }
            }
        }.launchIn(viewModelScope)
    }

    fun requestDownload(imageId: String, retry: Boolean = true) {
        downloadUseCase(token, imageId).onEach {  result ->
            when(result) {
                is Resource.Loading -> {
                    updateState(result, state = _downloadItemState, successData = null)
                }

                is Resource.Success -> {
                    updateState(result, state = _downloadItemState, successData = result.data)
                }

                else -> {
                    if (retry) {
                        setTokenAndTopic()
                        requestDownload(imageId, false)
                    } else {
                        updateState(result, state = _downloadItemState, successData = null)
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun setTokenAndTopic() {
        if (prefs.isClientLogin()) {
            token = prefs.getClientAccessToken() ?: ""
            topic = prefs.getClientFavoriteTopics()?.elementAt(0) ?: ""
        } else {
            token = prefs.getUserAccessToken() ?: ""
            topic = prefs.getUserFavoriteTopics()?.elementAt(0) ?: ""
        }
    }
}