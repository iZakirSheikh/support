/**
 * Kanged from Android Open Source Dialog.kt
 */

package com.primex.ui.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.*
import androidx.compose.ui.semantics.dialog
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastMap
import androidx.compose.ui.util.fastMaxBy
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import androidx.lifecycle.ViewTreeLifecycleOwner
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.*

@ExperimentalComposeApi
@Composable
fun BottomSheetDialog(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    properties: DialogProperties = DialogProperties(),
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current
    val composition = rememberCompositionContext()
    val currentContent by rememberUpdatedState(content)
    val dialogId = rememberSaveable { UUID.randomUUID() }

    val bottomSheetDialog = remember {
        BottomSheetDialogWrapper(
            onDismissRequest = onDismissRequest,
            properties = properties,
            composeView = view,
            layoutDirection = layoutDirection,
            density = density,
            dialogId = dialogId
        ).apply {
            setContent(composition) {
                BottomSheetDialogLayout(
                    modifier = Modifier
                        .semantics { dialog() }
                    //.nestedScroll(nestedScrollConnection),
                ) {
                    currentContent()
                }
            }
        }
    }

    DisposableEffect(bottomSheetDialog) {
        bottomSheetDialog.show()

        onDispose {
            bottomSheetDialog.dismiss()
            bottomSheetDialog.disposeComposition()
        }
    }

    SideEffect {
        bottomSheetDialog.updateParameters(
            onDismissRequest = onDismissRequest,
            properties = properties,
            layoutDirection = layoutDirection,
        )
        if (expanded)
            bottomSheetDialog.show()
        else
            bottomSheetDialog.hide()
    }
}

@OptIn(ExperimentalComposeUiApi::class)
private class BottomSheetDialogWrapper(
    private var onDismissRequest: () -> Unit,
    private var properties: DialogProperties,
    composeView: View,
    layoutDirection: LayoutDirection,
    density: Density,
    dialogId: UUID
) : BottomSheetDialog(composeView.context), ViewRootForInspector {

    private val bottomSheetDialogLayout: BottomSheetDialogLayout

    private val maxSupportedElevation = 30.dp

    override val subCompositionView: AbstractComposeView get() = bottomSheetDialogLayout

    init {
        val window = window ?: error("Dialog has no window")
        window.requestFeature(Window.FEATURE_NO_TITLE)
        window.setBackgroundDrawableResource(android.R.color.transparent)
        bottomSheetDialogLayout = BottomSheetDialogLayout(context, window).apply {
            tag = "BottomSheetDialog:$dialogId"
            clipChildren = false
            with(density) { elevation = maxSupportedElevation.toPx() }
        }

        fun ViewGroup.disableClipping() {
            clipChildren = false
            if (this is BottomSheetDialogLayout) return
            for (i in 0 until childCount) {
                (getChildAt(i) as? ViewGroup)?.disableClipping()
            }
        }

        (window.decorView as? ViewGroup)?.disableClipping()
        setContentView(bottomSheetDialogLayout)
        ViewTreeLifecycleOwner.set(bottomSheetDialogLayout, ViewTreeLifecycleOwner.get(composeView))
        ViewTreeViewModelStoreOwner.set(bottomSheetDialogLayout, ViewTreeViewModelStoreOwner.get(composeView))
        bottomSheetDialogLayout.setViewTreeSavedStateRegistryOwner(
            composeView.findViewTreeSavedStateRegistryOwner()
        )

        setOnDismissListener {
            onDismissRequest()
        }

        setCanceledOnTouchOutside(properties.dismissOnClickOutside)

        updateParameters(onDismissRequest, properties, layoutDirection)
    }

    fun setContent(
        parentComposition: CompositionContext,
        children: @Composable () -> Unit
    ) {
        bottomSheetDialogLayout.setContent(parentComposition, children)
    }

    private fun setLayoutDirection(layoutDirection: LayoutDirection) {
        bottomSheetDialogLayout.layoutDirection = when (layoutDirection) {
            LayoutDirection.Ltr -> android.util.LayoutDirection.LTR
            LayoutDirection.Rtl -> android.util.LayoutDirection.RTL
        }
    }

    fun updateParameters(
        onDismissRequest: () -> Unit,
        properties: DialogProperties,
        layoutDirection: LayoutDirection,
    ) {
        this.onDismissRequest = onDismissRequest
        this.properties = properties
        setLayoutDirection(layoutDirection)
    }

    fun disposeComposition() {
        bottomSheetDialogLayout.disposeComposition()
    }

    override fun onBackPressed() {
        if (properties.dismissOnBackPress) {
            onDismissRequest()
        }
    }
}



@SuppressLint("ViewConstructor")
private class BottomSheetDialogLayout(
    context: Context,
    override val window: Window
) : AbstractComposeView(context), DialogWindowProvider {

    private var content: @Composable () -> Unit by mutableStateOf({})

    override var shouldCreateCompositionOnAttachedToWindow: Boolean = false
        private set

    fun setContent(parent: CompositionContext, content: @Composable () -> Unit) {
        setParentCompositionContext(parent)
        this.content = content
        shouldCreateCompositionOnAttachedToWindow = true
        createComposition()
    }

    @Composable
    override fun Content() {
        content()
    }
}

@Composable
private fun BottomSheetDialogLayout(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        val placeables = measurables.fastMap { it.measure(constraints) }
        val width = placeables.fastMaxBy { it.width }?.width ?: constraints.minWidth
        val height = placeables.fastMaxBy { it.height }?.height ?: constraints.minHeight
        layout(width, height) {
            placeables.fastForEach { it.placeRelative(0, 0) }
        }
    }
}