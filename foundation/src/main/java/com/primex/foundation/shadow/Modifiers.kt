package com.primex.foundation.shadow


import android.graphics.BlurMaskFilter
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.DrawModifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private class ShadowModifierImpl(
    val outline: Outline,
    val offset: Offset,
    val filter: BlurMaskFilter,
    val light: Color,
    val dark: Color,
    val strokeWidth: Float = 0f
) : DrawModifier {
    override fun ContentDrawScope.draw() {

        val lightColorInt = light.toArgb()
        val darkShadowInt = dark.toArgb()

        val radiusPx =
            when (outline) {
                is PercentRoundedCornerOutline -> kotlin.run { size.minDimension * (outline.pct / 100f) }
                is DpRoundedCornerOutline -> outline.radius.toPx()
            }

        val isBg = strokeWidth == 0f

        when (isBg) {
            true -> background(
                offset = offset,
                radius = radiusPx,
                lightShadowColor = lightColorInt,
                darkShadowColor = darkShadowInt,
                filter = filter
            )
            else -> foreground(
                offset = offset,
                radius = radiusPx,
                lightShadowColor = lightColorInt,
                darkShadowColor = darkShadowInt,
                filter = filter,
                strokeWidth = strokeWidth
            )
        }

    }
}

private const val POINT_60 = 0.6f
private const val POINT_95 = 0.95f

fun Modifier.shadow(
    outline: Outline,
    light: Color,
    dark: Color,
    elevation: Dp,
    intensity: Float = Float.NaN,
    source: LightSource,
    border: BorderStroke? = null
): Modifier = composed {
    val elevationPx = kotlin.math.abs(with(LocalDensity.current) { elevation.toPx() })
    val elevated = elevation > 0.dp

    val multiplier = elevationPx * if (elevated) POINT_60 else POINT_95

    val blurRadius =
        (elevationPx * if (elevated) POINT_95 else POINT_60)
            //TODO: blurRadius Crash if equal to 0
            .coerceAtLeast(0.001f)

    val filter = BlurMaskFilter(blurRadius, BlurMaskFilter.Blur.NORMAL)

    val offset =
        source.toOffset(multiplier, elevated)

    shadow(
        outline = outline,
        offset = offset,
        filter = filter,
        light = if (elevated) light else dark,
        dark = if (elevated) dark else light,
        border = border,
        strokeWidth = if (elevated) 0f else multiplier,
        intensity = intensity
    )
}


fun Modifier.shadow(
    outline: Outline,
    offset: Offset,
    filter: BlurMaskFilter,
    strokeWidth: Float,
    light: Color,
    dark: Color,
    intensity: Float = Float.NaN,
    border: BorderStroke? = null
): Modifier {


    val lightM = if (intensity.isNaN()) light else light.copy(intensity)
    val darkM = if (intensity.isNaN()) dark else dark.copy(intensity)

    val shadow = when (offset) {
        Offset.Zero -> Modifier
        else -> ShadowModifierImpl(
            outline = outline,
            light = light,
            dark = dark,
            offset = offset,
            filter = filter,
            strokeWidth = strokeWidth
        )
    }

    val shapeAndroid = outline.asAndroidShape

    return this
        .then(shadow)
        .then(if (border != null) Modifier.border(border, shapeAndroid) else Modifier)
        .clip(shapeAndroid)
}