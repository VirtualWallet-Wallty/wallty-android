package com.krushkov.virtualwallet.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.krushkov.virtualwallet.R
import com.krushkov.virtualwallet.ui.utils.outerShadow

@Composable
fun AppHeader() {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.w_logo),
            contentDescription = " Logo",
            modifier = Modifier
                .height(32.dp)
                .outerShadow(
                    shape = RectangleShape,
                    color = Color.Black.copy(alpha = 0.15f),
                    offsetX = 0.dp,
                    offsetY = 0.dp,
                    blur = 8.dp
                )
        )
    }
}
