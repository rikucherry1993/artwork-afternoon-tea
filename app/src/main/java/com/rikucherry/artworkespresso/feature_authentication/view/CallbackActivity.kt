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
import androidx.lifecycle.ViewModelProvider
import com.rikucherry.artworkespresso.ArtworkEspressoApplication
import com.rikucherry.artworkespresso.common.theme.ArtworkEspressoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CallbackActivity : ComponentActivity() {

    private val viewModel : CallbackActivityViewModel by lazy {
        ViewModelProvider(this).get(CallbackActivityViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                        Text(text = "Callback Success")
                        viewModel.getAccessToken(intent, (application as ArtworkEspressoApplication).state)
                    }
                }
            }
        }
    }
}