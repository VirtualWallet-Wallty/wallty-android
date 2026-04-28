package com.krushkov.virtualwallet.ui.core

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.krushkov.virtualwallet.ui.theme.AppDialogShape
import com.krushkov.virtualwallet.ui.theme.Black

@Composable
fun Dialog(
    onDismissRequest: () -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    BackHandler { onDismissRequest() }

    Box(modifier = Modifier.fillMaxSize()) {
        GlassSurface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .align(Alignment.Center)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {},
            shape = AppDialogShape,
            containerColor = Black
        ) {
            Box(modifier = Modifier.padding(12.dp)) {
                content()
            }
        }
    }
}
