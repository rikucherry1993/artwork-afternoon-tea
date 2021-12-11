package com.rikucherry.artworkespresso.feature_topic_selection.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.rikucherry.artworkespresso.R
import com.rikucherry.artworkespresso.common.component.LineLoader
import com.rikucherry.artworkespresso.common.theme.Purple100
import com.rikucherry.artworkespresso.feature_topic_selection.presentation.viewmodel.TopicSelectViewModel

@Composable
fun TopTopicsScreen(
    viewModel: TopicSelectViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //TODO: Add font
        Text(
            fontSize = 32.sp,
            text = "MENU",
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colors.primary
        )
        Text(
            fontSize = 24.sp,
            text = "Follow up to 3 topics",
            modifier = Modifier.padding(16.dp)
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
        ) {
            items(state.data ?: emptyList()) { result ->
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
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