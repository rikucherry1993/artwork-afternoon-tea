package com.rikucherry.artworkespresso.feature_daily_brief.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.google.accompanist.insets.LocalWindowInsets
import com.rikucherry.artworkespresso.common.Constants
import com.rikucherry.artworkespresso.common.Constants.CollapsedAppBarHeight
import com.rikucherry.artworkespresso.common.Constants.ExpandedAppBarHeight
import com.rikucherry.artworkespresso.common.component.HeadingLevel
import com.rikucherry.artworkespresso.common.component.HeadingText
import com.rikucherry.artworkespresso.common.theme.BackgroundPrimary
import com.rikucherry.artworkespresso.common.theme.GrayParagraph
import com.rikucherry.artworkespresso.feature_daily_brief.presentation.viewmodel.DailyBriefViewModel
import kotlin.math.max

@Composable
fun DailyBriefScreen(
    viewModel: DailyBriefViewModel = hiltViewModel()
) {
    val scrollState = rememberLazyListState()

    Box(modifier = Modifier.fillMaxSize()) {
        DailyArtWorkList(scrollState)
        CollapsableToolBar(scrollState)

        IconButton(
            modifier = Modifier.size(CollapsedAppBarHeight, CollapsedAppBarHeight),
            onClick = { /*TODO*/ }
        ) {
            Icon(
                imageVector = Icons.Filled.Menu,
                contentDescription = "Menu button",
                modifier = Modifier.fillMaxSize().padding(8.dp, 4.dp)
            )
        }
    }
}

@Composable
fun CollapsableToolBar(scrollState: LazyListState) {
    val imageHeight = ExpandedAppBarHeight - CollapsedAppBarHeight
    val maxOffset = with(LocalDensity.current) {
        imageHeight.roundToPx()
    } - LocalWindowInsets.current.systemBars.top

    val offset = scrollState.firstVisibleItemScrollOffset.coerceAtMost(maxOffset)
    val offsetProgress = max(0f, offset * 2f - maxOffset * 1f) / maxOffset

    TopAppBar(
        modifier = Modifier
            .height(ExpandedAppBarHeight)
            .offset {
                IntOffset(x = 0, y = -offset)
            },
        backgroundColor = Color.Transparent,
        contentColor = GrayParagraph,
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(imageHeight)
                    .background(Color.Transparent)
            ) {
                Image(
                    painter = rememberImagePainter(
                        //TODO: Replace the placeholder
                        data = Constants.DEFAULT_AVATAR_URL
                    ),
                    contentScale = ContentScale.Fit,
                    contentDescription = "Top Artwork for today",
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            alpha = 1f - offsetProgress
                        }
                )

                // Add a vertical gradient from transparent to black to the bottom of the image
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    BackgroundPrimary
                                ),
                                startY = maxOffset * 0.9f
                            )
                        )
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(CollapsedAppBarHeight)
                    .background(MaterialTheme.colors.primary),
                verticalArrangement = Arrangement.Center,
            ) {
                HeadingText(
                    //TODO: Replace with current date
                    text = "Friday, Oct 29th",
                    headingLevel = HeadingLevel.SECONDARY,
                    color = GrayParagraph,
                    // Set horizontal margin to 8.dp
                    paddingLeft = 8.dp + (CollapsedAppBarHeight - 8.dp) * offsetProgress,
                )
            }
        }
    }
}

@Composable
fun DailyArtWorkList(scrollState: LazyListState) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(top = ExpandedAppBarHeight),
        state = scrollState
    ) {
        item {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                for (i in 0..100) {
                    Text("Text view: $i")
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}



