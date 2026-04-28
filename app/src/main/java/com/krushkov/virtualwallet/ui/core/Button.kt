package com.krushkov.virtualwallet.ui.core

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.size
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krushkov.virtualwallet.ui.theme.AppBorderStroke
import com.krushkov.virtualwallet.ui.theme.AppButtonShape
import com.krushkov.virtualwallet.ui.theme.Black
import com.krushkov.virtualwallet.ui.theme.CloudWhite
import com.krushkov.virtualwallet.ui.utils.innerShadow
import com.krushkov.virtualwallet.ui.utils.outerShadow

@Composable
private fun ButtonSurface(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = AppButtonShape,
    containerColor: Color = Black.copy(alpha = 0.5f),
    enabled: Boolean = true,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .outerShadow(shape)
            .clip(shape)
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Box(modifier = Modifier.matchParentSize().innerShadow(shape)) {
            Box(modifier = Modifier.fillMaxSize().background(containerColor))
        }
        Box(modifier = Modifier.matchParentSize().border(AppBorderStroke, shape))
        content()
    }
}

@Composable
fun Button(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = Black.copy(alpha = 0.5f),
    contentColor: Color = CloudWhite,
    enabled: Boolean = true,
    isLoading: Boolean = false
) {
    ButtonSurface(
        onClick = onClick,
        modifier = modifier,
        containerColor = containerColor,
        enabled = enabled && !isLoading
    ) {
        Text(
            text = text,
            color = contentColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun IconTextButton(
    icon: @Composable () -> Unit,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconSize: Dp = 24.dp,
    containerColor: Color = Black.copy(alpha = 0.5f),
    contentColor: Color = CloudWhite,
    enabled: Boolean = true
) {
    ButtonSurface(
        onClick = onClick,
        modifier = modifier,
        containerColor = containerColor,
        enabled = enabled
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(modifier = Modifier.size(iconSize)) { icon() }
            Text(
                text = text,
                color = contentColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun CircleButton(
    icon: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = Black.copy(alpha = 0.5f),
    enabled: Boolean = true
) {
    ButtonSurface(
        onClick = onClick,
        modifier = modifier,
        shape = CircleShape,
        containerColor = containerColor,
        enabled = enabled
    ) {
        Box(modifier = Modifier.padding(8.dp)) {
            icon()
        }
    }
}
