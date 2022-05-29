package com.rikucherry.artworkespresso.common.component

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter

@Composable
fun LineLoader(
    width: Dp = 5.dp,
    maxHeight: Dp = 24.dp,
    space: Dp = 5.dp,
    delayUnit: Int = 300,
    backgroundColor: Color = MaterialTheme.colors.primary
) {

    @Composable
    fun Line(
        scaleY: Float
    ) = Spacer(
        Modifier
            .size(width, maxHeight)
            .scale(1f, scaleY)
            .background(
                color = backgroundColor,
                shape = RectangleShape
            )
    )

    @Composable
    fun animateScaleWithDelay(delay: Int, maxScale: Float) = rememberInfiniteTransition()
        .animateFloat(
            initialValue = 0f,
            targetValue = 0f,
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = delayUnit * 5
                    0f at delay with LinearEasing
                    maxScale at delay + delayUnit with LinearEasing
                    0f at delay + delayUnit * 2 with LinearEasing
                }
            )
        )

    val scaleY1 by animateScaleWithDelay(0, 0.4f)
    val scaleY2 by animateScaleWithDelay(delayUnit, 0.6f)
    val scaleY3 by animateScaleWithDelay(delayUnit * 2, 0.8f)
    val scaleY4 by animateScaleWithDelay(delayUnit * 3, 1f)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Line(scaleY1)
        Spacer(Modifier.width(space))
        Line(scaleY2)
        Spacer(Modifier.width(space))
        Line(scaleY3)
        Spacer(Modifier.width(space))
        Line(scaleY4)
    }

}

@Composable
fun BkImageScaler(
    imageData: Any,
    imageDescription: String = "Background image",
    timeUnit: Int = 8_000,
    offset: Float = 50f
) {

    @Composable
    fun animateOffsetX() = rememberInfiniteTransition()
        .animateFloat(
            initialValue = 0f,
            targetValue = 0f,
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = timeUnit * 6
                    -offset at timeUnit with LinearEasing
                    -offset at timeUnit * 3 with LinearEasing
                    0f at timeUnit * 4 with LinearEasing
                }
            )
        )

    @Composable
    fun animateOffsetY() = rememberInfiniteTransition()
        .animateFloat(
            initialValue = 0f,
            targetValue = 0f,
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = timeUnit * 6
                    0f at timeUnit with LinearEasing
                    -offset * 2 at timeUnit * 3 with LinearEasing
                    -offset * 2 at timeUnit * 4 with LinearEasing
                }
            )
        )

    val offsetX by animateOffsetX()
    val offsetY by animateOffsetY()

    Image(painter = rememberImagePainter(data = imageData),
        contentDescription = imageDescription,
        modifier = Modifier.fillMaxSize()
            .alpha(0.3f)
            .scale(1.5f)
            .offset(offsetX.dp, offsetY.dp),
        contentScale = ContentScale.Crop
    )

}