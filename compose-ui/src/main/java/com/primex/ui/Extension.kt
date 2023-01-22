package com.primex.ui


import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.DialogProperties
import com.primex.ui.dialog.BottomSheetDialogProperties

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

@Composable
@NonRestartableComposable
fun IconButton(
    onClick: () -> Unit,
    imageVector: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    tint: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource
    ) {
        Icon(imageVector = imageVector, contentDescription = contentDescription, tint = tint)
    }
}


@Composable
@NonRestartableComposable
fun IconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    bitmap: ImageBitmap,
    contentDescription: String?,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    tint: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource
    ) {
        Icon(bitmap = bitmap, contentDescription = contentDescription, tint = tint)
    }
}

@Composable
@NonRestartableComposable
fun IconButton(
    onClick: () -> Unit,
    painter: Painter,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource
    ) {
        Icon(painter = painter, contentDescription = contentDescription, tint = tint)
    }
}


@Composable
@NonRestartableComposable
fun ColoredOutlineButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    elevation: ButtonElevation? = null,
    shape: Shape = RoundedCornerShape(50),
    colors: ButtonColors = ButtonDefaults.outlinedButtonColors(
        contentColor = MaterialTheme.colors.primary,
        disabledContentColor = MaterialTheme.colors.primary.copy(ContentAlpha.disabled),
        backgroundColor = Color.Transparent
    ),
    border: BorderStroke? = BorderStroke(
        2.dp,
        color = colors.contentColor(enabled = enabled).value
    ),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        elevation = elevation,
        shape = shape,
        border = border,
        colors = colors,
        contentPadding = contentPadding,
        content = content
    )
}

/**
 * A single line [Label] that is animated using the [AnimatedContent]
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedLabel(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign? = null,
    transitionSpec: AnimatedContentScope<AnnotatedString>.() -> ContentTransform = {
        slideInVertically { height -> height } + fadeIn() with
                slideOutVertically { height -> -height } + fadeOut()
    }
) {
    AnimatedContent(
        targetState = text,
        transitionSpec = transitionSpec,
        modifier = modifier,
        content = {
            Label(
                text = it,
                style = style,
                color = color,
                fontSize = fontSize,
                fontWeight = fontWeight,
                textAlign = textAlign
            )
        }
    )
}


@OptIn(ExperimentalAnimationApi::class)
@NonRestartableComposable
@Composable
inline fun AnimatedLabel(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign? = null,
    noinline transitionSpec: AnimatedContentScope<AnnotatedString>.() -> ContentTransform = {
        slideInVertically { height -> height } + fadeIn() with
                slideOutVertically { height -> -height } + fadeOut()
    }
) {
    AnimatedLabel(
        text = AnnotatedString(text),
        modifier = modifier,
        style = style,
        color = color,
        fontSize = fontSize,
        fontWeight = fontWeight,
        textAlign = textAlign,
        transitionSpec = transitionSpec
    )
}


/**
 * A *hack* replacement for Android Progress Indicator with path.
 */
@Composable
inline fun CircularProgressIndicator(
    /*@FloatRange(from = 0.0, to = 1.0)*/
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.primary,
    strokeWidth: Dp = ProgressIndicatorDefaults.StrokeWidth,
    backgroundColor: Color = color.copy(alpha = ProgressIndicatorDefaults.IndicatorBackgroundOpacity),
    noinline content: @Composable() (BoxScope.() -> Unit)? = null
) {
    Box(modifier = modifier, propagateMinConstraints = true) {


        val value by animateFloatAsState(targetValue = progress)

        val animatedColor by animateColorAsState(targetValue = color)

        if (progress.isNaN()) {
            CircularProgressIndicator(
                color = animatedColor,
                strokeWidth = strokeWidth
            )
        } else
            androidx.compose.material.CircularProgressIndicator(
                progress = value,
                color = animatedColor,
                strokeWidth = strokeWidth
            )

        if (content != null)
            content()

        //background
        androidx.compose.material.CircularProgressIndicator(
            progress = 1f,
            color = backgroundColor,
            strokeWidth = strokeWidth
        )
    }
}

@NonRestartableComposable
@Composable
inline fun DropDownMenuItem(
    title: AnnotatedString,
    noinline onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leading: Painter? = null,
    enabled: Boolean = true,
    contentPadding: PaddingValues = MenuDefaults.DropdownMenuItemContentPadding,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    DropdownMenuItem(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        contentPadding = contentPadding,
        interactionSource = interactionSource
    ) {
        if (leading != null)
            Icon(
                painter = leading,
                contentDescription = null,
                //  modifier = Modifier.padding(start = 16.dp)
            )

        // the text
        Label(
            text = title,
            modifier = Modifier.padding(horizontal = 16.dp),
            fontWeight = FontWeight.Medium
        )
    }
}

@NonRestartableComposable
@Composable
inline fun DropDownMenuItem(
    title: String,
    noinline onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leading: Painter? = null,
    enabled: Boolean = true,
    contentPadding: PaddingValues = MenuDefaults.DropdownMenuItemContentPadding,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) =
    DropDownMenuItem(
        title = AnnotatedString(title),
        onClick = onClick,
        modifier = modifier,
        leading = leading,
        enabled = enabled,
        contentPadding = contentPadding,
        interactionSource = interactionSource,
    )

@Composable
@NonRestartableComposable
inline fun Button(
    label: AnnotatedString,
    noinline onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leading: Painter? = null,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    elevation: ButtonElevation? = ButtonDefaults.elevation(),
    shape: Shape = MaterialTheme.shapes.small,
    border: BorderStroke? = null,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
) = Button(onClick = onClick,
    modifier = modifier,
    enabled = enabled,
    interactionSource = interactionSource,
    elevation = elevation,
    shape = shape,
    border = border,
    colors = colors,
    contentPadding = contentPadding,
    content = {
        if (leading != null)
            Icon(
                painter = leading,
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp)
            )
        Label(text = label)
    }
)

@Composable
@NonRestartableComposable
inline fun Button(
    label: String,
    noinline onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leading: Painter? = null,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    elevation: ButtonElevation? = ButtonDefaults.elevation(),
    shape: Shape = MaterialTheme.shapes.small,
    border: BorderStroke? = null,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
) = Button(
    onClick = onClick,
    label = AnnotatedString(label),
    modifier = modifier,
    leading = leading,
    enabled = enabled,
    interactionSource = interactionSource,
    elevation = elevation,
    shape = shape,
    border = border,
    colors = colors,
    contentPadding = contentPadding,
)

@Composable
@NonRestartableComposable
inline fun OutlinedButton(
    label: AnnotatedString,
    noinline onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leading: Painter? = null,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    elevation: ButtonElevation? = null,
    shape: Shape = MaterialTheme.shapes.small,
    border: BorderStroke? = ButtonDefaults.outlinedBorder,
    colors: ButtonColors = ButtonDefaults.outlinedButtonColors(),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding
) = OutlinedButton(
    onClick = onClick,
    modifier = modifier,
    enabled = enabled,
    interactionSource = interactionSource,
    elevation = elevation,
    shape = shape,
    border = border,
    colors = colors,
    contentPadding = contentPadding,
    content = {
        if (leading != null)
            Icon(
                painter = leading,
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp)
            )
        Label(text = label)
    },
)

@Composable
@NonRestartableComposable
inline fun OutlinedButton(
    label: String,
    noinline onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leading: Painter? = null,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    elevation: ButtonElevation? = null,
    shape: Shape = MaterialTheme.shapes.small,
    border: BorderStroke? = ButtonDefaults.outlinedBorder,
    colors: ButtonColors = ButtonDefaults.outlinedButtonColors(),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding
) = OutlinedButton(
    onClick = onClick,
    modifier = modifier,
    enabled = enabled,
    interactionSource = interactionSource,
    elevation = elevation,
    shape = shape,
    border = border,
    colors = colors,
    contentPadding = contentPadding,
    label = AnnotatedString(label),
    leading = leading
)

@Composable
@NonRestartableComposable
inline fun TextButton(
    label: AnnotatedString,
    noinline onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leading: Painter? = null,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    elevation: ButtonElevation? = null,
    shape: Shape = MaterialTheme.shapes.small,
    border: BorderStroke? = null,
    colors: ButtonColors = ButtonDefaults.textButtonColors(),
    contentPadding: PaddingValues = ButtonDefaults.TextButtonContentPadding,
) = TextButton(onClick = onClick,
    modifier = modifier,
    enabled = enabled,
    interactionSource = interactionSource,
    elevation = elevation,
    shape = shape,
    border = border,
    colors = colors,
    contentPadding = contentPadding,
    content = {
        if (leading != null)
            Icon(
                painter = leading,
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp)
            )
        Label(text = label)
    }
)

@Composable
@NonRestartableComposable
inline fun TextButton(
    label: String,
    noinline onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leading: Painter? = null,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    elevation: ButtonElevation? = null,
    shape: Shape = MaterialTheme.shapes.small,
    border: BorderStroke? = null,
    colors: ButtonColors = ButtonDefaults.textButtonColors(),
    contentPadding: PaddingValues = ButtonDefaults.TextButtonContentPadding,
) = TextButton(
    label = AnnotatedString(label),
    onClick = onClick,
    modifier = modifier,
    leading = leading,
    enabled = enabled,
    interactionSource = interactionSource,
    elevation = elevation,
    shape = shape,
    border = border,
    colors = colors,
    contentPadding = contentPadding
)


@Composable
inline fun Dialog(
    expanded: Boolean,
    noinline onDismissRequest: () -> Unit,
    properties: DialogProperties = DialogProperties(),
    noinline content: @Composable () -> Unit
) {
    if (expanded)
        androidx.compose.ui.window.Dialog(
            onDismissRequest = onDismissRequest,
            properties = properties,
            content = content
        )
}


@Composable
@ExperimentalComposeUiApi
inline fun BottomSheetDialog(
    expanded: Boolean,
    noinline onDismissRequest: () -> Unit,
    properties: BottomSheetDialogProperties = BottomSheetDialogProperties(dismissWithAnimation = true),
    noinline content: @Composable () -> Unit
){
    if (expanded)
        com.primex.ui.dialog.BottomSheetDialog(
            onDismissRequest = onDismissRequest,
            properties = properties,
            content = content,
        )
}