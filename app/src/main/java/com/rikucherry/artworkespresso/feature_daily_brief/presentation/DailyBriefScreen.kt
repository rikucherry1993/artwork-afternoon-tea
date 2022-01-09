package com.rikucherry.artworkespresso.feature_daily_brief.presentation

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.rikucherry.artworkespresso.feature_daily_brief.presentation.viewmodel.DailyBriefViewModel

@Composable
fun DailyBriefScreen(
    viewModel: DailyBriefViewModel = hiltViewModel()
) {
    Text("This is DailyBrief")
}