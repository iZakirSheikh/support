@file:Suppress("NOTHING_TO_INLINE")

package com.primex.foundation.shadow

import android.graphics.BlurMaskFilter
import androidx.annotation.ColorInt
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas

private inline val Offset.mirror get() = copy(-x, -y)

private inline fun Canvas.drawOutline(
    strokeWidth: Float = 0f,
    radius: Float,
    size: Size,
    paint: Paint
) {
    val (width, height) = size
    drawRoundRect(
        -strokeWidth,
        -strokeWidth,
        width + strokeWidth,
        height + strokeWidth,
        radius,
        radius,
        paint
    )
}

private inline fun Canvas.clipOutline(
    radius: Float,
    size: Size,
    clipOp: ClipOp = ClipOp.Intersect
) {
    val (width, height) = size
    val visiblePath = Path().also { p ->
        p.moveTo(0f, 0f)
        p.addRoundRect(
            RoundRect(
                left = 0f,
                top = 0f,
                right = width,
                bottom = height,
                radiusX = radius,
                radiusY = radius
            )
        )
    }
    clipPath(visiblePath, clipOp = clipOp)
}

/**
 * Draws shadow of given [color] & [size] .
 * @param offset: The [Offset] to translate the canvas by
 * @param size: The side of the canvas/shadow.
 * @param radius: The radius of the shadow.
 * @param color: The color of the shadow.
 * @param filter: The blur parameter to use to smooth the shadow.
 * @param strokeWidth: The width of the stroke. if equal to zero shadow will be drawn as fill otherwise as stroke.
 */
internal inline fun Canvas.shadow(
    size: Size,
    offset: Offset,
    radius: Float,
    @ColorInt color: Int,
    filter: BlurMaskFilter,
    strokeWidth: Float = 0f
) {
    // if stroke width isnot NAN draw foreground shadow else draw background shadow
    val isBg = strokeWidth == 0f

    val paint =
        Paint().also { paint ->
            paint.asFrameworkPaint().also { native ->
                native.isAntiAlias = true
                native.color = color
                native.maskFilter = filter
                // make paint for each shadow type separately
                when (isBg) {
                    true -> native.isDither = true
                    else -> {
                        native.strokeWidth = strokeWidth
                        native.style = android.graphics.Paint.Style.STROKE
                    }
                }
            }
        }

    val canvas = this
    canvas.save()
    // in case foreground clip it.
    if (!isBg) canvas.clipOutline(radius, size)
    canvas.translate(offset.x, offset.y)
    canvas.drawOutline(strokeWidth, radius, size, paint)
    canvas.restore()
}


internal inline fun ContentDrawScope.background(
    offset: Offset,
    radius: Float,
    @ColorInt lightShadowColor: Int,
    @ColorInt darkShadowColor: Int,
    filter: BlurMaskFilter,
) {
    drawIntoCanvas { canvas ->
        // light
        canvas.shadow(
            offset = offset,
            radius = radius,
            color = lightShadowColor,
            filter = filter,
            size = size
        )
        val mirrored = offset.mirror
        // dark
        canvas.shadow(
            offset = mirrored,
            radius = radius,
            color = darkShadowColor,
            filter = filter,
            size = size
        )
    }
    drawContent()
}


internal inline fun ContentDrawScope.foreground(
    offset: Offset,
    radius: Float,
    @ColorInt lightShadowColor: Int,
    @ColorInt darkShadowColor: Int,
    strokeWidth: Float,
    filter: BlurMaskFilter,
) {
    drawContent()

    drawIntoCanvas {canvas ->
        // draw light shadow
        val mirror = offset.mirror
        canvas.shadow(
            offset = mirror,
            color = lightShadowColor,
            radius = radius,
            filter = filter,
            strokeWidth = strokeWidth,
            size = size
        )

        // draw dar shadow
        canvas.shadow(
            offset = offset,
            color = darkShadowColor,
            radius = radius,
            filter = filter,
            strokeWidth = strokeWidth,
            size = size
        )
    }
}


/**
 * Draws the shadow around the content.
 * @param offset: number by which the canvas needs to be translated.
 * @param outline: The shape which provides the outline.
 * @param light: The color of the light shadow.
 * @param dark: The color of the dark shadow.
 * @param filter: The blur filter to use on shadow.
 */
private fun ContentDrawScope.shadow(
    offset: Offset,
    outline: Outline,
    light: Color,
    dark: Color,
    filter: BlurMaskFilter,
){
    TODO()
}