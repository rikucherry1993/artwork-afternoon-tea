package com.rikucherry.artworkespresso.common.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.rikucherry.artworkespresso.common.theme.BackgroundPrimary

/**
 * Add a vertical gradient from transparent to black to the bottom of the image
 *
 * Should be wrapped by a Box
 */
@Composable
fun ShadowedImage(
    imageData: String?,
    contentDescription: String?,
    imageModifier: Modifier,
    shadowModifier: Modifier,
    imageHeight: Number
) {
    Image(
        painter = rememberImagePainter(data = imageData),
        contentScale = ContentScale.Fit,
        contentDescription = contentDescription,
        modifier = imageModifier
    )

    // Add a vertical gradient from transparent to black to the bottom of the image
    Box(
        modifier = shadowModifier
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        BackgroundPrimary
                    ),
                    startY = imageHeight.toFloat() * 0.9f
                )
            )
    )
}

/**
 * Extension to Modifier for drawing colored shadow behind an image card
 *
 * @see <a href="https://gist.github.com/cedrickring/0497965b0658d6727aaec531f59e8c5c">original gist</a>
 */
fun Modifier.drawColoredShadow(
    color: Color,
    alpha: Float = 0.4f,
    borderRadius: Dp = 16.dp,
    shadowRadius: Dp = 6.dp,
    offsetY: Dp = 0.dp,
    offsetX: Dp = 0.dp
) = this.drawBehind {
    val transparentColor = Color.Transparent.toArgb()
    val shadowColor = color.copy(alpha = alpha).toArgb()
    this.drawIntoCanvas {
        val paint = Paint()
        val frameworkPaint = paint.asFrameworkPaint()
        frameworkPaint.color = transparentColor
        frameworkPaint.setShadowLayer(
            shadowRadius.toPx(),
            offsetX.toPx(),
            offsetY.toPx(),
            shadowColor
        )
        it.drawRoundRect(
            0f,
            0f,
            this.size.width,
            this.size.height,
            borderRadius.toPx(),
            borderRadius.toPx(),
            paint
        )
    }
}