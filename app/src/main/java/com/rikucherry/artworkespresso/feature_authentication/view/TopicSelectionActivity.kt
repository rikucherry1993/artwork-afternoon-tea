package com.rikucherry.artworkespresso.feature_authentication.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.rikucherry.artworkespresso.ArtworkEspressoApplication
import com.rikucherry.artworkespresso.common.Constants
import com.rikucherry.artworkespresso.common.theme.ArtworkEspressoTheme
import com.rikucherry.artworkespresso.common.tool.AssistedViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TopicSelectionActivity : ComponentActivity() {

    @Inject
    lateinit var callbackViewModelFactory: AssistedViewModel.AuthAssistedFactory
    lateinit var viewModel : LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args = Bundle().apply {
            this.putBoolean(Constants.IS_FREE_TRAIL,intent.getBooleanExtra(Constants.IS_FREE_TRAIL, false))
            this.putParcelable(Constants.AUTH_INTENT, intent)
            this.putString(Constants.AUTH_STATE, (application as ArtworkEspressoApplication).state)
            this.putBoolean(Constants.IS_TOPIC_EMPTY, true)
        }

        viewModel = AssistedViewModel.provideFactory(callbackViewModelFactory, args).create(LoginViewModel::class.java)

        setContent {
            ArtworkEspressoTheme {
                Surface(
                    color = MaterialTheme.colors.background
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("This is topic selection")
                        Text(text = viewModel.state.value.data ?: "")
                    }
                }
            }
        }
    }
}