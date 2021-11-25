package com.rikucherry.artworkespresso.common.tool

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rikucherry.artworkespresso.feature_authentication.presentation.LoginViewModel

class AssistedViewModel {
    @dagger.assisted.AssistedFactory
    interface AuthAssistedFactory {
        fun create(args: Bundle?): LoginViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: AuthAssistedFactory,
            args: Bundle?
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(args) as T
            }
        }
    }
}