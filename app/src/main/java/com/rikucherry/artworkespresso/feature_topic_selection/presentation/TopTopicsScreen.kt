package com.rikucherry.artworkespresso.feature_topic_selection.presentation

import android.content.Intent
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.rikucherry.artworkespresso.R
import com.rikucherry.artworkespresso.common.Constants
import com.rikucherry.artworkespresso.common.component.HeadingLevel
import com.rikucherry.artworkespresso.common.component.HeadingText
import com.rikucherry.artworkespresso.common.component.LineLoader
import com.rikucherry.artworkespresso.common.component.MenuButtonPrimary
import com.rikucherry.artworkespresso.common.theme.GrayParagraph
import com.rikucherry.artworkespresso.common.theme.Purple100
import com.rikucherry.artworkespresso.common.theme.Teal200
import com.rikucherry.artworkespresso.feature_daily_brief.presentation.DailyBriefActivity
import com.rikucherry.artworkespresso.feature_topic_selection.data.remote.data_source.TopTopicsDto
import com.rikucherry.artworkespresso.feature_topic_selection.presentation.viewmodel.TopicSelectViewModel
import com.skydoves.sandwich.StatusCode

@Composable
fun TopTopicsScreen(
    isFreeTrail: Boolean,
    viewModel: TopicSelectViewModel = hiltViewModel()
) {
    val topicState = viewModel.topicState.value
    val userState = viewModel.userState.value
    val loginInfoState = viewModel.loginInfoState.value

    val dataSize: Int = topicState.data?.size ?: 0 //Number of topics
    val rightColNum = dataSize / 2
    val leftColNum = dataSize - rightColNum

    val leftColData = topicState.data?.subList(0, leftColNum) ?: emptyList()
    val rightColData = topicState.data?.subList(leftColNum, dataSize) ?: emptyList()

    Column(
        modifier = Modifier.fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val selectedTopicState = remember {
            mutableStateOf("")
        }

        HeadingText(
            text = "MENU",
            headingLevel = HeadingLevel.PRIMARY,
            paddingTop = 32.dp,
            paddingBottom = 8.dp
        )
        HeadingText(
            text = "Follow 1 favourite topic",
            headingLevel = HeadingLevel.SECONDARY,
            paddingBottom = 24.dp,
            color = GrayParagraph
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(580.dp)
                .verticalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TopicsColumn(topics = leftColData, selectedTopicState)
            TopicsColumn(topics = rightColData, selectedTopicState)
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            MenuButtonPrimary(
                buttonDescription = "Continue",
                enabled = selectedTopicState.value.isNotEmpty(),
                widthFraction = 0.9f,
                height = 50.dp,
                onclick = {
                    viewModel.insertLoginInfo()
                }
            )
        }

        if (topicState.error.isNotBlank()) {
            //TODO: Temporary workaround
            Text(text = topicState.error)
        }

        if (userState.error.isNotBlank()) {
            //TODO: Temporary workaround
            Text(text = userState.error)
        }

        if (loginInfoState.error.isNotBlank()) {
            //TODO: Temporary workaround
            Text(text = loginInfoState.error)
        }

        if (topicState.isLoading || userState.isLoading || loginInfoState.isLoading) {
            LineLoader(
                backgroundColor = Purple100
            )
        }

        if (loginInfoState.status == StatusCode.OK) {
            viewModel.saveFavouriteTopic(selectedTopicState.value)
            val context = LocalContext.current
            val intent = Intent(context, DailyBriefActivity::class.java).apply {
                this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                this.putExtra(Constants.IS_AUTHENTICATED, true)
                this.putExtra(Constants.IS_FREE_TRAIL, isFreeTrail)
            }
            context.startActivity(intent)
        }
    }

}

@Composable
fun TopicsColumn(topics: List<TopTopicsDto>, selectedTopicState: MutableState<String>) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        topics.forEach { result ->

            val isSelected = result.name == selectedTopicState.value

            Image(
                painter = rememberImagePainter(
                    data = result.exampleDeviations?.get(0)?.content?.src ?: "",
                    builder = {
                        transformations(CircleCropTransformation())
                        placeholder(drawableResId = R.drawable.placeholder_topics)
                    }
                ),
                contentDescription = result.exampleDeviations?.get(0)?.title ?: "",
                modifier = Modifier
                    .size(116.dp)
                    .border(
                        width = 5.dp,
                        color = if (isSelected) Teal200 else Color.Transparent,
                        shape = CircleShape
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ) {
                        if (!isSelected) {
                            selectedTopicState.value = result.name
                        } else {
                            selectedTopicState.value = ""
                        }
                    }
            )
            Spacer(modifier = Modifier.height(5.dp))
            HeadingText(
                text = result.name,
                headingLevel = HeadingLevel.PARAGRAPH,
                color = GrayParagraph,
                paddingBottom = 5.dp
            )
        }

    }
}