package com.krushkov.virtualwallet.ui.features.cards.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krushkov.virtualwallet.domain.models.outputs.card.Card
import com.krushkov.virtualwallet.ui.theme.*

@Composable
fun CardItem(
    card: Card,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(110.dp)
            .border(AppBorderStroke, CardItemShape)
            .background(NightBlack, shape = CardItemShape)
            .padding(12.dp)
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = "CARD",
                color = CloudWhite,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "••${card.cardSuffix}",
                color = CloudWhite,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun CardItemShimmer() {
    Box(
        modifier = Modifier
            .width(180.dp)
            .height(110.dp)
            .clip(AppCardShape)
            .shimmerEffect()
    )
}
