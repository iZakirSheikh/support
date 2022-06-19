package com.primex.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ProgressButton(
    progress: Float,
    text: String,
    modifier: Modifier = Modifier,
    borderStroke: BorderStroke? = null,
    color: Color = MaterialTheme.colors.primary,
    onClick: () -> Unit
) {
    val new by animateColorAsState(
        targetValue = color,
        animationSpec = tween(250)
    )

    val progressT by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(250)
    )

    TextButton(
        onClick = onClick, modifier = modifier,
        colors = ButtonDefaults.textButtonColors(
            contentColor = suggestContentColorFor(new)
        ),
        contentPadding = PaddingValues(0.dp),
        border = borderStroke
    ) {

        Box(modifier = Modifier.fillMaxWidth()) {

            LinearProgressIndicator(
                progress = progressT,
                color = color,
                modifier = Modifier.fillMaxSize(),
            )

            Label(
                text = text,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}
