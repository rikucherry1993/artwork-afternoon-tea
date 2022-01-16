package com.rikucherry.artworkespresso.feature_authentication.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rikucherry.artworkespresso.ArtworkEspressoApplication
import com.rikucherry.artworkespresso.R
import com.rikucherry.artworkespresso.common.Constants
import com.rikucherry.artworkespresso.common.component.LineLoader
import com.rikucherry.artworkespresso.common.component.MenuButtonPrimary
import com.rikucherry.artworkespresso.common.component.MenuButtonSecondary
import com.rikucherry.artworkespresso.common.theme.ArtworkEspressoTheme
import com.rikucherry.artworkespresso.common.theme.Purple100
import com.rikucherry.artworkespresso.feature_authentication.data.local.data_source.LoginStatus
import com.rikucherry.artworkespresso.feature_authentication.presentation.viewmodel.EntranceViewModel
import com.rikucherry.artworkespresso.feature_daily_brief.presentation.DailyBriefActivity
import com.rikucherry.artworkespresso.feature_topic_selection.presentation.TopicSelectionActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EntranceActivity : ComponentActivity() {

    private val viewModel: EntranceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getLoginStatus()

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
                        val state = viewModel.state.value
                        when {
                            state.isLoading -> {
                                LineLoader(
                                    backgroundColor = Purple100
                                )
                            }
                            state.data == LoginStatus.USER_LOGGED_IN -> {
                                val intent =
                                    Intent(this@EntranceActivity, DailyBriefActivity::class.java)
                                intent.putExtra(Constants.IS_FREE_TRAIL, false)
                                intent.putExtra(Constants.IS_AUTHENTICATED, true)
                                startActivity(intent)
                                finish()
                            }
                            state.data == LoginStatus.CLIENT_LOGGED_IN -> {
                                val intent =
                                    Intent(this@EntranceActivity, DailyBriefActivity::class.java)
                                intent.putExtra(Constants.IS_FREE_TRAIL, true)
                                intent.putExtra(Constants.IS_AUTHENTICATED, true)
                                startActivity(intent)
                                finish()
                            }
                            else -> {
                                MenuButtonPrimary(
                                    buttonDescription = stringResource(R.string.button_primary_text),
                                ) {
                                    val requestState = (application as ArtworkEspressoApplication).state
                                    val isTopicEmpty = viewModel.getUserTopics()?.isEmpty() ?: true
                                    val intent = Intent(Intent.ACTION_VIEW).apply {
                                        this.data = viewModel.formAuthorizeUri(requestState, isTopicEmpty)
                                    }
                                    startActivity(intent)
                                    finish()
                                }
                                Spacer(modifier = Modifier.height(32.dp))
                                MenuButtonSecondary(
                                    buttonDescription = stringResource(R.string.button_secondary_text)
                                ) {
                                    val isTopicEmpty =
                                        viewModel.getClientTopics()?.isEmpty() ?: true
                                    intent = if (isTopicEmpty) {
                                        Intent(
                                            this@EntranceActivity,
                                            TopicSelectionActivity::class.java
                                        )
                                    } else {
                                        Intent(
                                            this@EntranceActivity,
                                            DailyBriefActivity::class.java
                                        )
                                    }
                                    intent.putExtra(Constants.IS_FREE_TRAIL, true)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}