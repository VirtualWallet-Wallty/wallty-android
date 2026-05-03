package com.krushkov.virtualwallet.ui.features.cards.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krushkov.virtualwallet.R
import com.krushkov.virtualwallet.domain.models.outputs.card.Card
import com.krushkov.virtualwallet.domain.models.outputs.card.CardStatus
import com.krushkov.virtualwallet.ui.core.Button
import com.krushkov.virtualwallet.ui.core.IconTextButton
import com.krushkov.virtualwallet.ui.theme.CloudWhite
import com.krushkov.virtualwallet.ui.theme.CyanNeon
import com.krushkov.virtualwallet.ui.theme.Green
import com.krushkov.virtualwallet.ui.theme.Red
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CardsHeroSection(
    cards: List<Card>,
    selectedCard: Card?,
    onCardSelected: (Card) -> Unit,
    onTopUpClick: () -> Unit,
    onCardStatusActionClick: () -> Unit,
    onRemoveClick: () -> Unit,
    onAddCardClick: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { cards.size })

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            if (cards.isNotEmpty()) {
                onCardSelected(cards[page])
            }
        }
    }

    val addCardIcon = remember {
        ImageVector.Builder(
            name = "AddCard",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 16f,
            viewportHeight = 16f
        ).path(
            stroke = SolidColor(Color.White),
            strokeLineWidth = 1f,
            strokeLineCap = StrokeCap.Butt,
            strokeLineJoin = StrokeJoin.Miter
        ) {
            moveTo(3f, 8f)
            horizontalLineTo(13f)
            moveTo(8f, 3f)
            verticalLineTo(13f)
        }.build()
    }

    val removeIcon = remember {
        ImageVector.Builder(
            name = "Remove",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 16f,
            viewportHeight = 16f
        ).path(
            stroke = SolidColor(Color.White),
            strokeLineWidth = 1f,
            strokeLineCap = StrokeCap.Butt,
            strokeLineJoin = StrokeJoin.Miter
        ) {
            moveTo(3f, 8f)
            horizontalLineTo(13f)
        }.build()
    }

    val topUpIcon = remember {
        ImageVector.Builder(
            name = "TopUp",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 16f,
            viewportHeight = 16f
        ).path(
            stroke = SolidColor(Color.White),
            strokeLineWidth = 1f,
            strokeLineCap = StrokeCap.Butt,
            strokeLineJoin = StrokeJoin.Miter
        ) {
            moveTo(2f, 12.9999f)
            horizontalLineTo(14f)
            moveTo(2f, 13.9999f)
            horizontalLineTo(14f)
            moveTo(7.97856f, 13.957f)
            lineTo(7.97856f, 2.95703f)
            moveTo(11.5f, 6.22849f)
            lineTo(7.97856f, 2.70703f)
            lineTo(4.45708f, 6.22849f)
        }.build()
    }

    val activateIcon = remember {
        ImageVector.Builder(
            name = "Activate",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 16f,
            viewportHeight = 16f
        ).path(
            stroke = SolidColor(Color.White),
            strokeLineWidth = 1f,
            strokeLineCap = StrokeCap.Butt,
            strokeLineJoin = StrokeJoin.Round
        ) {
            moveTo(11.5f, 8f)
            lineTo(5.5f, 4.5f)
            verticalLineTo(11.5f)
            lineTo(11.5f, 8f)
            close()
        }.build()
    }

    val deactivateIcon = remember {
        ImageVector.Builder(
            name = "Deactivate",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 16f,
            viewportHeight = 16f
        ).path(
            stroke = SolidColor(Color.White),
            strokeLineWidth = 1f,
            strokeLineCap = StrokeCap.Butt,
            strokeLineJoin = StrokeJoin.Round
        ) {
            moveTo(4.5f, 4.5f)
            horizontalLineTo(6.5f)
            verticalLineTo(11.5f)
            horizontalLineTo(4.5f)
            verticalLineTo(4.5f)
            close()
        }.path(
            stroke = SolidColor(Color.White),
            strokeLineWidth = 1f,
            strokeLineCap = StrokeCap.Butt,
            strokeLineJoin = StrokeJoin.Round
        ) {
            moveTo(9.5f, 4.5f)
            horizontalLineTo(11.5f)
            verticalLineTo(11.5f)
            horizontalLineTo(9.5f)
            verticalLineTo(4.5f)
            close()
        }.build()
    }

    val restrictedIcon = remember {
        ImageVector.Builder(
            name = "Restricted",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 16f,
            viewportHeight = 16f
        ).path(
            stroke = SolidColor(Color.White),
            strokeLineWidth = 1f,
            strokeLineCap = StrokeCap.Butt,
            strokeLineJoin = StrokeJoin.Miter
        ) {
            moveTo(10.5f, 7f)
            verticalLineTo(4f)
            curveTo(10.5f, 2.61929f, 9.38071f, 1.5f, 8f, 1.5f)
            curveTo(6.61929f, 1.5f, 5.5f, 2.61929f, 5.5f, 4f)
            verticalLineTo(7f)
            moveTo(8f, 10f)
            curveTo(7.72386f, 10f, 7.5f, 10.2239f, 7.5f, 10.5f)
            curveTo(7.5f, 10.7761f, 7.72386f, 11f, 8f, 11f)
            curveTo(8.27614f, 11f, 8.5f, 10.7761f, 8.5f, 10.5f)
            curveTo(8.5f, 10.2239f, 8.27614f, 10f, 8f, 10f)
            close()
            moveTo(8f, 10f)
            verticalLineTo(12.5f)
            moveTo(3.5f, 7.5f)
            horizontalLineTo(12.5f)
            verticalLineTo(14.5f)
            horizontalLineTo(3.5f)
            verticalLineTo(7.5f)
            close()
        }.build()
    }

    Column {
        Text(
            text = stringResource(R.string.title_cards),
            color = CloudWhite,
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 96.dp),
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer { clip = false },
            pageSpacing = 12.dp,
            beyondBoundsPageCount = 1,
            verticalAlignment = Alignment.CenterVertically
        ) { page ->
            val card = cards[page]
            val pageOffset =
                ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue

            Box(
                modifier = Modifier.fillMaxWidth().padding(24.dp, 0.dp),
                contentAlignment = Alignment.Center
            ) {
                CardItem(
                    card = card,
                    modifier = Modifier
                        .graphicsLayer {
                            clip = false
                            val scale = 1f - (pageOffset * 0.12f).coerceIn(0f, 0.12f)
                            scaleX = scale
                            scaleY = scale
                            alpha = 1f - (pageOffset * 0.4f).coerceIn(0f, 0.4f)
                        }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        val isTopUpDisabled = selectedCard?.status == CardStatus.USER_DEACTIVATED ||
                selectedCard?.status == CardStatus.ADMIN_DEACTIVATED

        IconTextButton(
            icon = {
                Icon(
                    painter = rememberVectorPainter(image = addCardIcon),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
            },
            text = stringResource(R.string.action_add_card),
            onClick = onAddCardClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            containerColor = CyanNeon.copy(alpha = 0.5f)
        )

        if (selectedCard != null) {
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isTopUpDisabled) {
                    IconTextButton(
                        icon = {
                            Icon(
                                painter = rememberVectorPainter(image = restrictedIcon),
                                contentDescription = null,
                                tint = Color.Unspecified
                            )
                        },
                        text = stringResource(R.string.action_topup),
                        onClick = {},
                        modifier = Modifier.weight(1f),
                        enabled = false
                    )
                } else {
                    IconTextButton(
                        icon = {
                            Icon(
                                painter = rememberVectorPainter(image = topUpIcon),
                                contentDescription = null,
                                tint = Color.Unspecified
                            )
                        },
                        text = stringResource(R.string.action_topup),
                        onClick = onTopUpClick,
                        modifier = Modifier.weight(0.8f),
                        containerColor = Green.copy(alpha = 0.5f)
                    )
                }

                when (selectedCard.status) {
                    CardStatus.ACTIVE -> IconTextButton(
                        icon = {
                            Icon(
                                painter = rememberVectorPainter(image = deactivateIcon),
                                contentDescription = null,
                                tint = Color.Unspecified
                            )
                        },
                        text = stringResource(R.string.action_deactivate),
                        onClick = onCardStatusActionClick,
                        modifier = Modifier.weight(1f),
                        containerColor = CyanNeon.copy(alpha = 0.5f)
                    )

                    CardStatus.USER_DEACTIVATED -> IconTextButton(
                        icon = {
                            Icon(
                                painter = rememberVectorPainter(image = activateIcon),
                                contentDescription = null,
                                tint = Color.Unspecified
                            )
                        },
                        text = stringResource(R.string.action_activate),
                        onClick = onCardStatusActionClick,
                        modifier = Modifier.weight(1f),
                        containerColor = CyanNeon.copy(alpha = 0.5f)
                    )

                    CardStatus.ADMIN_DEACTIVATED -> IconTextButton(
                        icon = {
                            Icon(
                                painter = rememberVectorPainter(image = restrictedIcon),
                                contentDescription = null,
                                tint = Color.Unspecified
                            )
                        },
                        text = stringResource(R.string.action_restricted),
                        onClick = {},
                        modifier = Modifier.weight(1f),
                        enabled = false
                    )

                    else -> Spacer(modifier = Modifier.weight(1f))
                }

                IconTextButton(
                    icon = {
                        Icon(
                            painter = rememberVectorPainter(image = removeIcon),
                            contentDescription = null,
                            tint = Color.Unspecified
                        )
                    },
                    text = stringResource(R.string.action_remove),
                    onClick = onRemoveClick,
                    modifier = Modifier.weight(0.9f),
                    containerColor = Red.copy(alpha = 0.5f)
                )
            }

        }
    }
}

@Composable
fun CardsHeroSectionShimmer() {
    Column {
        Text(
            text = "Cards",
            color = CloudWhite,
            fontSize = 24.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            userScrollEnabled = false
        ) {
            items(3) {
                CardItemShimmer()
            }
        }
    }
}
