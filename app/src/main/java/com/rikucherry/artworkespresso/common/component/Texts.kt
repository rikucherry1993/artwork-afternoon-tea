package com.rikucherry.artworkespresso.common.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rikucherry.artworkespresso.R
import com.rikucherry.artworkespresso.common.theme.PurplePrimary

val robotSlabFamily = FontFamily(
    Font(R.font.robotoslab_light, FontWeight.Light),
    Font(R.font.robotoslab_regular, FontWeight.Normal),
    Font(R.font.robotoslab_medium, FontWeight.Medium),
    Font(R.font.robotoslab_bold, FontWeight.Bold),
    Font(R.font.robotoslab_extrabold, FontWeight.ExtraBold),
    Font(R.font.robotoslab_black, FontWeight.Black),
)

enum class HeadingLevel {
    PRIMARY, SECONDARY, THIRD, PARAGRAPH
}

val headTextSizeMap = mutableMapOf(
    HeadingLevel.PRIMARY to 32.sp,
    HeadingLevel.SECONDARY to 24.sp,
    HeadingLevel.THIRD to 18.sp,
    HeadingLevel.PARAGRAPH to 14.sp
)

val headTextWeightMap = mutableMapOf(
    HeadingLevel.PRIMARY to FontWeight.Bold,
    HeadingLevel.SECONDARY to FontWeight.Medium,
    HeadingLevel.THIRD to FontWeight.Normal,
    HeadingLevel.PARAGRAPH to FontWeight.Normal
)

@Composable
fun HeadingText(
    text: String,
    headingLevel: HeadingLevel = HeadingLevel.SECONDARY,
    color: Color = PurplePrimary,
    paddingLeft: Dp = 0.dp,
    paddingRight: Dp = 0.dp,
    paddingTop: Dp = 0.dp,
    paddingBottom: Dp = 0.dp,
    modifier:Modifier = Modifier
) {

    val fontSize = headTextSizeMap[headingLevel]!!
    val fontWeight = headTextWeightMap[headingLevel]!!

    Text(
        fontSize = fontSize,
        text = text,
        fontFamily = robotSlabFamily,
        fontWeight = fontWeight,
        modifier = modifier.padding(paddingLeft,paddingTop,paddingRight,paddingBottom),
        color = color,
        maxLines = 1
    )
}