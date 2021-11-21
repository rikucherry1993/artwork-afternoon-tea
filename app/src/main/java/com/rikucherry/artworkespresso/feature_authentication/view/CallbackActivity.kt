package com.rikucherry.artworkespresso.feature_authentication.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.rikucherry.artworkespresso.ArtworkEspressoApplication
import com.rikucherry.artworkespresso.common.theme.ArtworkEspressoTheme
import com.rikucherry.artworkespresso.feature_authentication.domain.util.AuthenticationUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CallbackActivity : ComponentActivity() {

    private val viewModel : CallbackActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val authCode = AuthenticationUtil.retrieveAuthorizeCode(intent,(application as ArtworkEspressoApplication).state)
        viewModel.getAccessToken(authCode)

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
                    }
                }
            }
        }
    }
}