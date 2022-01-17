package com.rikucherry.artworkespresso.feature_daily_brief.presentation

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.rikucherry.artworkespresso.ArtworkEspressoApplication
import com.rikucherry.artworkespresso.common.Constants
import com.rikucherry.artworkespresso.common.component.LineLoader
import com.rikucherry.artworkespresso.common.theme.ArtworkEspressoTheme
import com.rikucherry.artworkespresso.common.theme.Purple100
import com.rikucherry.artworkespresso.common.tool.AssistedViewModel
import com.rikucherry.artworkespresso.feature_authentication.presentation.EntranceActivity
import com.rikucherry.artworkespresso.feature_authentication.presentation.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DailyBriefActivity : ComponentActivity() {

    @Inject
    lateinit var callbackViewModelFactory: AssistedViewModel.AuthAssistedFactory
    lateinit var loginViewModel : LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isAuthenticated = intent.getBooleanExtra(Constants.IS_AUTHENTICATED, false)
        val isFreeTrail = intent.getBooleanExtra(Constants.IS_FREE_TRAIL, false)

        if (!isAuthenticated) {
            val args = Bundle().apply {
                this.putBoolean(Constants.IS_FREE_TRAIL,isFreeTrail)
                this.putParcelable(Constants.AUTH_INTENT, intent)
                this.putString(Constants.AUTH_STATE, (application as ArtworkEspressoApplication).state)
                this.putBoolean(Constants.IS_TOPIC_EMPTY, false)
            }

            loginViewModel = AssistedViewModel.provideFactory(callbackViewModelFactory, args).create(
                LoginViewModel::class.java)
        }


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
                        if (!isAuthenticated) {
                            val loginState = loginViewModel.state.value
                            when {
                                loginState.isLoading -> {
                                    LineLoader(
                                        backgroundColor = Purple100
                                    )
                                }

                                loginState.error.isNotBlank() -> {
                                    val errorMessage = loginState.error
                                    //TODO: Temporary workaround
                                    Toast.makeText(this@DailyBriefActivity,
                                        "Authentication Error: $errorMessage", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this@DailyBriefActivity, EntranceActivity::class.java))
                                    finish()
                                }

                                else -> {
                                    DailyBriefScreen(isFreeTrail)
                                }
                            }
                        } else {
                            DailyBriefScreen(isFreeTrail)
                        }
                    }
                }
            }
        }
    }
}