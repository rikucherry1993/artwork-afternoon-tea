package com.rikucherry.artworkespresso.feature_authentication.view

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.rikucherry.artworkespresso.BuildConfig
import com.rikucherry.artworkespresso.Secrets
import com.rikucherry.artworkespresso.common.Constants.AUTH_RESPONSE_TYPE
import com.rikucherry.artworkespresso.common.Constants.AUTH_VIEW
import com.rikucherry.artworkespresso.common.Constants.FULL_SCOPE
import com.rikucherry.artworkespresso.common.Constants.REDIRECT_URI
import com.rikucherry.artworkespresso.feature_authentication.data.repository.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(authRepository: AuthenticationRepository) : ViewModel() {

    private val clientId = Secrets().getClientId(BuildConfig.APPLICATION_ID)
    private val clientSecrets = Secrets().getClientSecret(BuildConfig.APPLICATION_ID)
    private val state = UUID.randomUUID().toString()
    private val authRepository = authRepository

    fun formAuthorizeUri(): Uri {
        return authRepository.formAuthorizeUri(
            responseType = AUTH_RESPONSE_TYPE,
            clientId = clientId,
            redirectUri = REDIRECT_URI,
            scope = FULL_SCOPE,
            state = state,
            view = AUTH_VIEW
        )
    }

}