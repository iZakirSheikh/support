package com.primex.core

import androidx.compose.foundation.Indication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.layout

private fun Modifier.gradient(
    colors: List<Color>,
    brushProvider: (List<Color>, Size) -> Brush
): Modifier = composed {
    var size by rememberState(initial = Size.Zero)
    val gradient = remember(colors, size) { brushProvider(colors, size) }
    drawWithContent {
        size = this.size
        drawContent()
        drawRect(brush = gradient)
    }
}

/**
 * Adds [gradient] over content.
 * [colors] determine top -> bottom or bottom -> top orientation.
 *
 * Note: default is with [Color.Black] and bottom -> top.
 */
fun Modifier.verticalGradient(
    colors: List<Color> = listOf(
        Color.Transparent,
        Color.Black,
    )
) = gradient(colors) { gradientColors, size ->
    Brush.verticalGradient(
        colors = gradientColors,
        startY = 0f,
        endY = size.height
    )
}

/**
 * @see [verticalGradient]
 */
fun Modifier.horizontalGradient(
    colors: List<Color> = listOf(
        Color.Black,
        Color.Transparent
    )
) = gradient(colors) { gradientColors, size ->
    Brush.horizontalGradient(
        colors = gradientColors,
        startX = 0f,
        endX = size.width
    )
}


/**
 * @see [Brush.radialGradient]
 */
fun Modifier.radialGradient(
    colors: List<Color> = listOf(
        Color.Transparent,
        Color.Black
    ),
    center: Offset = Offset.Unspecified,
    radius: Float = Float.POSITIVE_INFINITY,
    tileMode: TileMode = TileMode.Clamp
): Modifier = gradient(colors) { gradientColors, size ->
    Brush.radialGradient(
        colors = gradientColors,
        center = center,
        radius = radius,
        tileMode = tileMode
    )
}


/**
 * The utility function rotates transforms the composable to clockwise and anti-clockwise.
 */
fun Modifier.rotate(clockwise: Boolean): Modifier {
    val transform = Modifier.layout { measurable, constraints ->
        // as rotation is taking place
        // the height becomes so construct new set of construnts from old one.
        val newConstraints = constraints.copy(
            minWidth = constraints.minHeight,
            minHeight = constraints.minWidth,
            maxHeight = constraints.maxWidth,
            maxWidth = constraints.maxHeight
        )

        // measure measurable with new constraints.
        val placeable = measurable.measure(newConstraints)

        layout(placeable.height, placeable.width) {

            //Compute where to place the measurable.
            // TODO needs to rethink these
            val x = -(placeable.width / 2 - placeable.height / 2)
            val y = -(placeable.height / 2 - placeable.width / 2)

            placeable.place(x = x, y = y)
        }
    }

    val rotated = Modifier.rotate(if (clockwise) 90f else -90f)

    // transform and then apply rotation.
    return this
        .then(transform)
        .then(rotated)
}


/**
 * This modifier acquires focus to this widget as soon as the user clicks.
 */
fun Modifier.acquireFocusOnInteraction(
    interactionSource: MutableInteractionSource? = null,
    indication: Indication? = null
): Modifier = composed {
    val interaction = interactionSource ?: remember {
        MutableInteractionSource()
    }
    val requester = remember {
        FocusRequester()
    }
    val isFocused by interaction.collectIsFocusedAsState()
    Modifier
        .focusRequester(requester)
        .focusable(true, interactionSource = interaction)
        .clickable(
            enabled = !isFocused,
            indication = indication,
            onClick = { requester.requestFocus() },
            interactionSource = remember {
                MutableInteractionSource()
            }
        )
        .then(this)
}