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
import androidx.compose.ui.unit.dp
import com.rikucherry.artworkespresso.ArtworkEspressoApplication
import com.rikucherry.artworkespresso.common.Constants
import com.rikucherry.artworkespresso.common.component.MenuButtonPrimary
import com.rikucherry.artworkespresso.common.component.MenuButtonSecondary
import com.rikucherry.artworkespresso.common.theme.ArtworkEspressoTheme
import com.rikucherry.artworkespresso.feature_authentication.presentation.viewmodel.EntranceViewModel
import com.rikucherry.artworkespresso.feature_daily_brief.presentation.DailyBriefActivity
import com.rikucherry.artworkespresso.feature_topic_selection.presentation.TopicSelectionActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EntranceActivity : ComponentActivity() {

    private val viewModel: EntranceViewModel by viewModels()

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
                            val state = (application as ArtworkEspressoApplication).state
                            val isTopicEmpty = viewModel.getUserTopics()?.isEmpty() ?: true
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                this.data = viewModel.formAuthorizeUri(state, isTopicEmpty)
                            }
                            startActivity(intent)
                        }
                        Spacer(modifier = Modifier.height(32.dp))
                        MenuButtonSecondary(
                            buttonDescription = "Start Trail Now"
                        ) {
                            val isTopicEmpty = viewModel.getClientTopics()?.isEmpty() ?:true
                            intent = if (isTopicEmpty) {
                                Intent(this@EntranceActivity, TopicSelectionActivity::class.java)
                            } else {
                                Intent(this@EntranceActivity, DailyBriefActivity::class.java)
                            }
                            intent.putExtra(Constants.IS_FREE_TRAIL, true)
                            startActivity(intent)
                        }
                    }
                }
            }
        }
    }
}