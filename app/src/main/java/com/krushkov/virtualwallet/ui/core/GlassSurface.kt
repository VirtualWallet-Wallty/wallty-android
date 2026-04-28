package com.krushkov.virtualwallet.ui.core

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import com.krushkov.virtualwallet.ui.theme.AppBorderStroke
import com.krushkov.virtualwallet.ui.theme.Black
import com.krushkov.virtualwallet.ui.utils.innerShadow
import com.krushkov.virtualwallet.ui.utils.outerShadow

@Composable
fun GlassSurface(
    modifier: Modifier = Modifier,
    shape: Shape,
    containerColor: Color = Black.copy(alpha = 0.5f),
    showOuterShadow: Boolean = true,
    showInnerShadow: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .then(if (showOuterShadow) Modifier.outerShadow(shape) else Modifier)
            .clip(shape)
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .then(if (showInnerShadow) Modifier.innerShadow(shape) else Modifier)
        ) {
            Box(modifier = Modifier.fillMaxSize().background(containerColor))
        }

        Box(modifier = Modifier.matchParentSize().border(AppBorderStroke, shape))

        content()
    }
}
