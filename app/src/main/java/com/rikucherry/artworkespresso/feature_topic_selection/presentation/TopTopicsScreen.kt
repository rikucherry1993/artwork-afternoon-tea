package com.rikucherry.artworkespresso.feature_topic_selection.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.rikucherry.artworkespresso.common.component.LineLoader
import com.rikucherry.artworkespresso.common.theme.Purple100
import com.rikucherry.artworkespresso.feature_topic_selection.presentation.viewmodel.TopicSelectViewModel

@Composable
fun TopTopicsScreen(
    viewModel: TopicSelectViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(state.data ?: emptyList()) { result ->
                Text(result.name)
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