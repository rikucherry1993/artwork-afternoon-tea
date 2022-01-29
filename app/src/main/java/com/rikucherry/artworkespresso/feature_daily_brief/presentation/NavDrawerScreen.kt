package com.rikucherry.artworkespresso.feature_daily_brief.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.rikucherry.artworkespresso.R
import com.rikucherry.artworkespresso.common.Constants
import com.rikucherry.artworkespresso.common.component.HeadingLevel
import com.rikucherry.artworkespresso.common.component.HeadingText
import com.rikucherry.artworkespresso.common.theme.BackgroundPrimary
import com.rikucherry.artworkespresso.common.theme.GrayDark
import com.rikucherry.artworkespresso.common.theme.GrayParagraph

val weeklyDates = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

@Composable
fun NavDrawerScreen(isFreeTrail: Boolean) {
    val userName: String
    val iconUrl: String

    if (isFreeTrail) {
        userName = "Client"
        iconUrl = Constants.DEFAULT_AVATAR_URL
    } else {
        userName = "User Name"
        iconUrl = Constants.DEFAULT_AVATAR_URL
        //TODO:get from login info table
    }

    Column (
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxHeight(0.7f),
                verticalAlignment = Alignment.Bottom
            ) {
                Image(
                    modifier = Modifier
                        .size(100.dp)
                        .clickable {
                            //TODO: Move to user screen
                        },
                    painter = rememberImagePainter(
                        data = iconUrl,
                        builder = {
                            transformations(CircleCropTransformation())
                            placeholder(R.drawable.placeholder_person_foreground)
                        }
                    ),
                    contentScale = ContentScale.Fit,
                    contentDescription = "Author Icon",
                )
            }

            Row (
                modifier = Modifier.fillMaxHeight(),
                verticalAlignment = Alignment.Top){
                HeadingText(
                    text = userName,
                    color = GrayParagraph,
                    headingLevel = HeadingLevel.THIRD,
                    paddingTop = 8.dp
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                for (i in weeklyDates.indices) {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, BackgroundPrimary, RectangleShape),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = GrayDark
                        ),
                        onClick = { onWeeklyDateClicked(i)} //TODO
                    ) {
                        HeadingText(
                            text = weeklyDates[i],
                            color = GrayParagraph,
                            headingLevel = HeadingLevel.SECONDARY,
                            paddingTop = 4.dp,
                            paddingBottom = 4.dp
                        )
                    }
                }
            }
        }
    }
}

fun onWeeklyDateClicked(index: Int) {
    //Todo
}

@Preview
@Composable
fun Preview(){
    NavDrawerScreen(false)
}