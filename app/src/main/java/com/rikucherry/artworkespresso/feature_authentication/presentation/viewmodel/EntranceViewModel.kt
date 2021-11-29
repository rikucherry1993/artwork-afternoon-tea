package com.rikucherry.artworkespresso.feature_authentication.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.rikucherry.artworkespresso.common.tool.SharedPreferenceHelper
import com.rikucherry.artworkespresso.feature_authentication.domain.use_case.UserLoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EntranceViewModel @Inject constructor(
    private val userLoginUseCase: UserLoginUseCase,
    private val prefs: SharedPreferenceHelper,
) : ViewModel() {

    fun formAuthorizeUri(state: String, isTopicEmpty: Boolean) =
        userLoginUseCase.formAuthorizeUri(state, isTopicEmpty)

    fun getUserTopics(): MutableSet<String>? = prefs.getUserFavoriteTopics()

    fun getClientTopics(): MutableSet<String>? = prefs.getClientFavoriteTopics()

}