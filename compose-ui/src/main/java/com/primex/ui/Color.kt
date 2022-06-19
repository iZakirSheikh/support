package com.primex.ui

import androidx.annotation.FloatRange
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.luminance
import kotlin.math.max
import kotlin.math.min


private val CapriBlue = Color(0xFF0D47A1)
private val SkyBlue = Color(0xFF1A73E8)
private val LightBlue = Color(0xFF03A9F4)
private val Orange = Color(0xFFF4491f)
private val Rose = Color(0xFFE92635)
private val OrientRed = Color(0xffae1c27)
private val RedViolet = Color(0xFF991c37)
private val ClaretViolet = Color(0xFF740945)
private val Magenta = Color(0xFFE91E63)
private val SignalBlue = Color(0xFF4A148C)
private val AzureBlue = Color(0xFF006064)
private val MetroGreen = Color(0xFF00A300)
private val MetroGreen2 = Color(0xFF4CAF50)
private val OliveYellow = Color(0xFF8BC34A)
private val Ivory = Color(0xFFCDDC39)
private val TrafficYellow = Color(0xFFffc107)
private val DahliaYellow = Color(0xFFff9800)
private val Amber = Color(0xFFFF6F00)
private val BlackOlive = Color(0xFF383838)
private val SepiaBrown = Color(0xFF38220f)
private val UmbraGrey = Color(0xFF333333)
private val SignalWhite = Color(0xFFF2F2F2)
private val JetBlack = Color(0xFF121114)
private val TrafficBlack = Color(0xFF1D1D1E)

val Color.Companion.UmbraGrey
    get() = com.primex.ui.UmbraGrey
val Color.Companion.SignalWhite
    get() = com.primex.ui.SignalWhite
val Color.Companion.Amber
    get() = com.primex.ui.Amber
val Color.Companion.Orange
    get() = com.primex.ui.Orange
val Color.Companion.Rose
    get() = com.primex.ui.Rose
val Color.Companion.RedViolet
    get() = com.primex.ui.RedViolet
val Color.Companion.ClaretViolet
    get() = com.primex.ui.ClaretViolet
val Color.Companion.MetroGreen2
    get() = com.primex.ui.MetroGreen2
val Color.Companion.MetroGreen
    get() = com.primex.ui.MetroGreen
val Color.Companion.LightBlue
    get() = com.primex.ui.LightBlue
val Color.Companion.SkyBlue
    get() = com.primex.ui.SkyBlue
val Color.Companion.BlueLilac
    get() = SignalBlue
val Color.Companion.CapriBlue
    get() = com.primex.ui.CapriBlue
val Color.Companion.AzureBlue
    get() = com.primex.ui.AzureBlue
val Color.Companion.TrafficYellow
    get() = com.primex.ui.TrafficYellow
val Color.Companion.DahliaYellow
    get() = com.primex.ui.DahliaYellow
val Color.Companion.BlackOlive
    get() = com.primex.ui.BlackOlive
val Color.Companion.SepiaBrown
    get() = com.primex.ui.SepiaBrown
val Color.Companion.OrientRed
    get() = com.primex.ui.OrientRed
val Color.Companion.Ivory
    get() = com.primex.ui.Ivory
val Color.Companion.OliveYellow
    get() = com.primex.ui.OliveYellow
val Color.Companion.Magenta
    get() = com.primex.ui.Magenta
val Color.Companion.JetBlack
    get() = com.primex.ui.JetBlack
val Color.Companion.TrafficBlack
    get() = com.primex.ui.TrafficBlack

/**
 * Suggest Color
 * Generates a color that can be represented on provided background [Color] based on the luminance
 * @param backgroundColor - provided background color
 * @return [Color.White] if luminance < 0.3 otherwise [Color.Black]
 */

fun suggestContentColorFor(backgroundColor: Color): Color {
    return if (backgroundColor.luminance() < 0.3f) Color.White else Color.Black
}


fun Color.contrastAgainst(background: Color): Float {
    val fg = if (alpha < 1f) compositeOver(background) else this
    val fgLuminance = fg.luminance() + 0.05f
    val bgLuminance = background.luminance() + 0.05f

    return max(fgLuminance, bgLuminance) / min(fgLuminance, bgLuminance)
}


/**
 * Blend between two [Color]s using the given ratio.
 * A blend ratio of 0.0 will result in color1, 0.5 will give an even blend, 1.0 will result in color2.
 * @params: color1 – the first ARGB color
 * @param color – the second ARGB color
 * @param ratio – the blend ratio of color1 to color2
 */
fun Color.blend(color: Color, @FloatRange(from = 0.0, to = 1.0) ratio: Float): Color {
    val inverseRatio = 1 - ratio
    val a = alpha * inverseRatio + color.alpha * ratio
    val r = red * inverseRatio + color.red * ratio
    val g = green * inverseRatio + color.green * ratio
    val b = blue * inverseRatio + color.blue * ratio
    return Color(r, g, b, a, colorSpace = ColorSpaces.Srgb)
}