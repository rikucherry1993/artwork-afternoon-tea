package com.rikucherry.artworkespresso.feature_authentication.presentation

import androidx.lifecycle.ViewModel
import com.rikucherry.artworkespresso.feature_authentication.domain.use_case.UserLoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EntranceViewModel @Inject constructor(
    private val userLoginUseCase: UserLoginUseCase
) : ViewModel() {

    fun formAuthorizeUri(state: String, isTopicEmpty: Boolean) =
        userLoginUseCase.formAuthorizeUri(state, isTopicEmpty)

}