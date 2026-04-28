package com.krushkov.virtualwallet.ui.features.cards.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.krushkov.virtualwallet.ui.theme.AppCardShape
import com.krushkov.virtualwallet.ui.theme.NightBlack

@Composable
fun GlassCard(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clip(AppCardShape)
            .background(NightBlack)
            .padding(16.dp)
    ) {
        content()
    }
}
