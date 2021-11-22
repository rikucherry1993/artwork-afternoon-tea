package com.rikucherry.artworkespresso.feature_authentication.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rikucherry.artworkespresso.ArtworkEspressoApplication
import com.rikucherry.artworkespresso.common.component.MenuButtonPrimary
import com.rikucherry.artworkespresso.common.component.MenuButtonSecondary
import com.rikucherry.artworkespresso.common.theme.ArtworkEspressoTheme
import com.rikucherry.artworkespresso.feature_authentication.domain.use_case.UserLoginUseCase

class EntranceActivity : ComponentActivity() {

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
                        MenuButtonPrimary(
                            buttonDescription = "Login in with Deviant Art",
                        ) {
                            val intent = Intent(Intent.ACTION_VIEW)
                            val state = (application as ArtworkEspressoApplication).state
                            intent.data = UserLoginUseCase.formAuthorizeUri(state)
                            startActivity(intent)
                        }
                        Spacer(modifier = Modifier.height(32.dp))
                        MenuButtonSecondary(
                            buttonDescription = "Start Trail Now"
                        ) {
                            //todo: Implement logic
                        }
                    }
                }
            }
        }
    }
}