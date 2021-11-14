package com.rikucherry.artworkespresso.feature_authentication.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rikucherry.artworkespresso.common.component.MenuButtonPrimary
import com.rikucherry.artworkespresso.common.component.MenuButtonSecondary
import com.rikucherry.artworkespresso.common.theme.ArtworkEspressoTheme

class LogInActivity : ComponentActivity() {
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
                            //todo: Implement logic
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