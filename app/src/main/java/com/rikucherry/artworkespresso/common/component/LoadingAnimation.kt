package com.rikucherry.artworkespresso.common.component

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

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