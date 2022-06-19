package com.primex.ui

import androidx.compose.runtime.Composable
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*

@NonRestartableComposable
@Composable
fun Label(
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign? = null,
    maxLines: Int = 1,
    text: AnnotatedString,
) {
    Text(
        text = text,
        modifier = modifier,
        style = style,
        maxLines = maxLines,
        color = color,
        overflow = TextOverflow.Ellipsis,
        fontWeight = fontWeight,
        fontSize = fontSize,
        textAlign = textAlign
    )
}

@NonRestartableComposable
@Composable
fun Label(
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign? = null,
    maxLines: Int = 1,
    text: String,
) = Label(
    modifier = modifier,
    style = style,
    color = color,
    maxLines = maxLines,
    fontWeight = fontWeight,
    text = AnnotatedString(text),
    fontSize = fontSize,
    textAlign = textAlign
)

@NonRestartableComposable
@Composable
fun Header(
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.h6,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = style.fontSize,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign? = null,
    maxLines: Int = 1,
    text: AnnotatedString,
) = Label(
    modifier = modifier,
    style = style,
    color = color,
    maxLines = maxLines,
    fontWeight = fontWeight,
    text = text,
    fontSize = fontSize,
    textAlign = textAlign
)

@NonRestartableComposable
@Composable
fun Header(
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.h6,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = style.fontSize,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign? = null,
    maxLines: Int = 1,
    text: String,
) = Label(
    modifier = modifier,
    style = style,
    color = color,
    maxLines = maxLines,
    fontWeight = fontWeight,
    text = AnnotatedString(text),
    fontSize = fontSize,
    textAlign = textAlign
)


@NonRestartableComposable
@Composable
fun Caption(
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign? = null,
    maxLines: Int = 1,
    text: AnnotatedString,
) = Label(
    text = text,
    modifier = modifier,
    style = MaterialTheme.typography.caption,
    color = color,
    fontWeight = fontWeight,
    textAlign = textAlign,
    maxLines = maxLines
)

@NonRestartableComposable
@Composable
fun Caption(
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign? = null,
    maxLines: Int = 1,
    text: String,
) = Caption(
    text = AnnotatedString(text),
    modifier = modifier,
    color = color,
    fontWeight = fontWeight,
    textAlign = textAlign,
    maxLines = maxLines
)