@file:Suppress("NOTHING_TO_INLINE", "FunctionName")

package com.primex.foundation.shadow

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * The [Outline] marker interface.
 * TODO: Future version will surely include much more advanced features.
 */
sealed interface Outline

/**
 * Construct a  Circular [Outline] shape
 */
val CircularOutline get() = RoundedCornerOutline(50)

/**
 * Constructs The [Outline] Round Shape.
 *
 * @param: radius of the shape in [Dp].
 */
@JvmInline
internal value class DpRoundedCornerOutline(val radius: Dp) : Outline

/**
 * Constructs the [Outline] with pct size of corner
 */
@JvmInline
internal value class PercentRoundedCornerOutline(val pct: Int) : Outline


/**
 * Construct a [Outline] shape with corners equal to [percent] radius
 * @param percent: radius in percent between 0 to 100
 */
fun RoundedCornerOutline(percent: Int = 0): Outline =
    PercentRoundedCornerOutline(percent)

/**
 * Construct a [Outline] shape with corners equal to [radius]
 * @param radius: radius in [Dp]
 */
fun RoundedCornerOutline(radius: Dp = 0.dp): Outline =
    DpRoundedCornerOutline(radius = radius)

val Outline.asAndroidShape
    get() =
        when (this) {
            is DpRoundedCornerOutline -> RoundedCornerShape(radius)
            is PercentRoundedCornerOutline -> if (pct == 50) CircleShape else RoundedCornerShape(
                pct
            )
        }
