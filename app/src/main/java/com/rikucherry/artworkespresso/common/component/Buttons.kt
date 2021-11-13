package com.rikucherry.artworkespresso.common.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rikucherry.artworkespresso.ui.theme.ButtonTextPrimary

@Composable
fun MenuButtonPrimary(
    buttonDescription: String,
    widthFraction: Float = 0.8f,
    height: Dp = 60.dp,
    fontSize: TextUnit = 20.sp,
    backgroundColor: Color = MaterialTheme.colors.primary,
    textColor: Color = ButtonTextPrimary,
    onclick: () -> Unit
) {
    Button(
        enabled = true,
        onClick = onclick,
        colors = ButtonDefaults.textButtonColors(
            backgroundColor = backgroundColor
        ),
        modifier = Modifier
            .fillMaxWidth(widthFraction)
            .height(height)
    ) {
        Text(
            textAlign = TextAlign.Center,
            text = buttonDescription,
            fontSize = fontSize,
            color = textColor
        )
    }
}


@Composable
fun MenuButtonSecondary(
    buttonDescription: String,
    widthFraction: Float = 0.8f,
    height: Dp = 60.dp,
    fontSize: TextUnit = 20.sp,
    color: Color = MaterialTheme.colors.primary,
    strokeWidth: Dp = 2.dp,
    onclick: () -> Unit
) {
    OutlinedButton(
        enabled = true,
        onClick = onclick,
        border = BorderStroke(strokeWidth, color),
        modifier = Modifier
            .fillMaxWidth(widthFraction)
            .height(height)
    ) {
        Text(
            textAlign = TextAlign.Center,
            text = buttonDescription,
            fontSize = fontSize,
            color = color
        )
    }
}