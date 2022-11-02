package com.primex.ui

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Resources
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit

@Composable
fun ProvideTextStyle(
    style: TextStyle,
    alpha: Float = LocalContentAlpha.current,
    color: Color = LocalContentColor.current,
    textSelectionColors: TextSelectionColors = LocalTextSelectionColors.current,
    content: @Composable () -> Unit,
) {
    val mergedStyle = LocalTextStyle.current.merge(style)
    CompositionLocalProvider(
        LocalContentColor provides color,
        LocalContentAlpha provides alpha,
        LocalTextSelectionColors provides textSelectionColors,
        LocalTextStyle provides mergedStyle,
        content = content
    )
}

@Composable
fun ProvideTextStyle(
    style: TextStyle = LocalTextStyle.current,
    alpha: Float = LocalContentAlpha.current,
    color: Color = LocalContentColor.current,
    textSelectionColors: TextSelectionColors = LocalTextSelectionColors.current,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    content: @Composable () -> Unit,
) {
    // NOTE(text-perf-review): It might be worthwhile writing a bespoke merge implementation that
    // will avoid reallocating if all of the options here are the defaults
    val mergedStyle =
        style.merge(
            TextStyle(
                color = color.copy(alpha),
                fontSize = fontSize,
                fontWeight = fontWeight,
                textAlign = textAlign,
                lineHeight = lineHeight,
                fontFamily = fontFamily,
                textDecoration = textDecoration,
                fontStyle = fontStyle,
                letterSpacing = letterSpacing
            )
        )

    CompositionLocalProvider(
        LocalTextSelectionColors provides textSelectionColors,
        LocalTextStyle provides mergedStyle,
        content = content
    )
}


/**
 * A Utility function that finds and returns the [Activity] within [Context]
 */
private tailrec fun Context.findActivity(): Activity =
    when (this) {
        is Activity -> this
        is ContextWrapper -> this.baseContext.findActivity()
        else -> throw IllegalStateException(
            "Context is not an Activity context, but a ${javaClass.simpleName} context. "
        )
    }

/**
 * Provides/finds a [Activity] that is wrapped inside the [LocalContext]
 */
val ProvidableCompositionLocal<Context>.activity
    @ReadOnlyComposable
    @Composable
    get() = current.findActivity()

/**
 * Returns a Resources instance for the application's package.
 */
val ProvidableCompositionLocal<Context>.resources: Resources
    @ReadOnlyComposable
    @Composable
    inline get() = current.resources

/**
 * The recommended divider Alpha
 */
val ContentAlpha.Divider
    get() = com.primex.ui.Divider
private const val Divider = 0.12f


/**
 * The recommended LocalIndication Alpha
 */
val ContentAlpha.Indication
    get() = com.primex.ui.Indication
private const val Indication = 0.1f

