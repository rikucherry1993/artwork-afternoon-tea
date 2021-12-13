package com.rikucherry.artworkespresso.feature_topic_selection.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.rikucherry.artworkespresso.R
import com.rikucherry.artworkespresso.common.component.HeadingLevel
import com.rikucherry.artworkespresso.common.component.HeadingText
import com.rikucherry.artworkespresso.common.component.LineLoader
import com.rikucherry.artworkespresso.common.theme.GrayParagraph
import com.rikucherry.artworkespresso.common.theme.Purple100
import com.rikucherry.artworkespresso.feature_topic_selection.data.remote.data_source.TopTopicsDto
import com.rikucherry.artworkespresso.feature_topic_selection.presentation.viewmodel.TopicSelectViewModel

@Composable
fun TopTopicsScreen(
    viewModel: TopicSelectViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val dataSize: Int = state.data?.size ?: 0 //Number of topics
    val rightColNum = dataSize / 2
    val leftColNum = dataSize - rightColNum

    val leftColData = state.data?.subList(0, leftColNum) ?: emptyList()
    val rightColData = state.data?.subList(leftColNum - 1, dataSize) ?: emptyList()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeadingText(
            text = "MENU",
            headingLevel = HeadingLevel.PRIMARY,
            paddingTop = 32.dp,
            paddingBottom = 8.dp
        )
        HeadingText(
            text = "Follow up to 3 topics",
            headingLevel = HeadingLevel.SECONDARY,
            paddingBottom = 24.dp,
            color = GrayParagraph
        )


        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TopicsColumn(data = leftColData)
            TopicsColumn(data = rightColData)
        }

        if (state.error.isNotBlank()) {
            //TODO: Temporary workaround
            Text(text = state.error)
        }

        if (state.isLoading) {
            LineLoader(
                backgroundColor = Purple100
            )
        }
    }

}

@Composable
fun TopicsColumn(data: List<TopTopicsDto>) {
    Column(
        modifier = Modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        data.forEach { result ->
            Image(
                painter = rememberImagePainter(
                    data = result.exampleDeviations?.get(0)?.content?.src ?: "",
                    builder = {
                        transformations(CircleCropTransformation())
                        placeholder(drawableResId = R.drawable.placeholder_topics)
                    }
                ),
                contentDescription = null,
                modifier = Modifier.size(128.dp)
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(result.name)
        }

    }
}