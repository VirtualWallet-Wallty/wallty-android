package com.krushkov.virtualwallet.ui.features.home.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krushkov.virtualwallet.domain.models.outputs.currency.Currency
import com.krushkov.virtualwallet.domain.models.outputs.wallet.Wallet
import com.krushkov.virtualwallet.ui.core.Button
import com.krushkov.virtualwallet.ui.core.CircleButton
import com.krushkov.virtualwallet.ui.core.IconTextButton
import com.krushkov.virtualwallet.ui.theme.*

@Composable
fun HeroWalletSection(
    wallet: Wallet,
    isAllWalletsVisible: Boolean,
    isEditingWallets: Boolean,
    onToggleAllWallets: (Boolean) -> Unit,
    onToggleEditingWallets: () -> Unit,
    onCancelEditingWallets: () -> Unit,
    onTopUpClick: () -> Unit,
    onMoveClick: () -> Unit,
    onTransferClick: () -> Unit,
    currencies: Map<String, Currency> = emptyMap()
) {
    val walletName = wallet.name
    val currency = wallet.currency?.symbol ?: currencies[wallet.currencyCode]?.symbol ?: wallet.currencyCode ?: ""
    val balanceText = "${wallet.balance} $currency"

    Crossfade(
        targetState = isAllWalletsVisible,
        animationSpec = tween(durationMillis = 300),
        label = "WalletSectionTransition"
    ) { visible ->
        if (visible) {
            AllWalletsHeader(
                isEditing = isEditingWallets,
                onCloseClick = { 
                    if (isEditingWallets) {
                        onCancelEditingWallets()
                    } else {
                        onToggleAllWallets(false)
                    }
                },
                onEditClick = onToggleEditingWallets
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 0.dp, start = 20.dp, end = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = walletName,
                    color = CloudWhite,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = balanceText,
                    color = CloudWhite,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black
                )

                Spacer(modifier = Modifier.height(12.dp))

                val transferIcon = androidx.compose.runtime.remember {
                    ImageVector.Builder(
                        name = "Transfer",
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
                        moveTo(10.8f, 14f); verticalLineTo(12.3f)
                        curveTo(10.8f, 11.1954f, 9.90452f, 10.3f, 8.79995f, 10.3f)
                        horizontalLineTo(7.19995f)
                        curveTo(6.09538f, 10.3f, 5.19995f, 11.1954f, 5.19995f, 12.3f); verticalLineTo(14f)
                        moveTo(14.5f, 10.5f); verticalLineTo(9.3f)
                        curveTo(14.5f, 8.19543f, 13.6046f, 7.3f, 12.5f, 7.3f); horizontalLineTo(11f)
                        moveTo(1.5f, 10.5f); verticalLineTo(9.3f)
                        curveTo(1.5f, 8.19543f, 2.39543f, 7.3f, 3.5f, 7.3f); horizontalLineTo(5f)
                        moveTo(9.60546f, 7.10544f)
                        curveTo(9.60546f, 7.9921f, 8.88668f, 8.71088f, 8.00002f, 8.71088f)
                        curveTo(7.11336f, 8.71088f, 6.39458f, 7.9921f, 6.39458f, 7.10544f)
                        curveTo(6.39458f, 6.21878f, 7.11336f, 5.5f, 8.00002f, 5.5f)
                        curveTo(8.88668f, 5.5f, 9.60546f, 6.21878f, 9.60546f, 7.10544f); close()
                        moveTo(13.4055f, 4.10544f)
                        curveTo(13.4055f, 4.9921f, 12.6867f, 5.71088f, 11.8001f, 5.71088f)
                        curveTo(10.9134f, 5.71088f, 10.1946f, 4.9921f, 10.1946f, 4.10544f)
                        curveTo(10.1946f, 3.21878f, 10.9134f, 2.5f, 11.8001f, 2.5f)
                        curveTo(12.6867f, 2.5f, 13.4055f, 3.21878f, 13.4055f, 4.10544f); close()
                        moveTo(5.90551f, 4.10544f)
                        curveTo(5.90551f, 4.9921f, 5.18673f, 5.71088f, 4.30007f, 5.71088f)
                        curveTo(3.41341f, 5.71088f, 2.69463f, 4.9921f, 2.69463f, 4.10544f)
                        curveTo(2.69463f, 3.21878f, 3.41341f, 2.5f, 4.30007f, 2.5f)
                        curveTo(5.18673f, 2.5f, 5.90551f, 3.21878f, 5.90551f, 4.10544f); close()
                    }.build()
                }

                val addIcon = androidx.compose.runtime.remember {
                    ImageVector.Builder(
                        name = "Add",
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
                        moveTo(3f, 8f); lineTo(13f, 8f)
                        moveTo(8f, 3f); lineTo(8f, 13f)
                    }.build()
                }

                IconTextButton(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.AccountBalanceWallet,
                            contentDescription = null,
                            tint = CloudWhite
                        )
                    },
                    text = "All Wallets",
                    onClick = { onToggleAllWallets(true) },
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = CyanNeon.copy(alpha = 0.5f)
                )

                Spacer(modifier = Modifier.height(8.dp))

                val moveIcon = androidx.compose.runtime.remember {
                    ImageVector.Builder(
                        name = "Move",
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
                        moveTo(14f, 9.5f)
                        horizontalLineTo(3f)
                        lineTo(7f, 12.5f)
                        moveTo(2f, 6.5f)
                        horizontalLineTo(13f)
                        lineTo(9f, 3f)
                    }.build()
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconTextButton(
                        icon = {
                            Icon(
                                painter = rememberVectorPainter(image = addIcon),
                                contentDescription = null,
                                tint = Color.Unspecified
                            )
                        },
                        text = "Add money",
                        onClick = onTopUpClick,
                        modifier = Modifier.weight(1f),
                        containerColor = Green.copy(alpha = 0.5f)
                    )
                    IconTextButton(
                        icon = {
                            Icon(
                                painter = rememberVectorPainter(image = moveIcon),
                                contentDescription = null,
                                tint = Color.Unspecified
                            )
                        },
                        text = "Move",
                        onClick = onMoveClick,
                        modifier = Modifier.weight(0.75f),
                        containerColor = CyanNeon.copy(alpha = 0.5f)
                    )
                    IconTextButton(
                        icon = {
                            Icon(
                                painter = rememberVectorPainter(image = transferIcon),
                                contentDescription = null,
                                tint = Color.Unspecified
                            )
                        },
                        text = "Transfer",
                        onClick = onTransferClick,
                        modifier = Modifier.weight(0.9f),
                        containerColor = CyanNeon.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}

@Composable
fun AllWalletsHeader(
    isEditing: Boolean,
    onCloseClick: () -> Unit,
    onEditClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(74.dp)
    ) {
        Text(
            text = "All Wallets",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val arrowBackIcon = ImageVector.Builder(
                name = "ArrowBack",
                defaultWidth = 32.dp,
                defaultHeight = 32.dp,
                viewportWidth = 32f,
                viewportHeight = 32f
            ).path(
                stroke = SolidColor(Color(0xFFF5F5F5)),
                strokeLineWidth = 3f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(25.3334f, 16f)
                lineTo(6.66669f, 16f)
                moveTo(6.66669f, 16f)
                lineTo(16f, 25.3334f)
                moveTo(6.66669f, 16f)
                lineTo(16f, 6.66669f)
            }.build()

            if (isEditing) {
                CircleButton(
                    icon = {
                        Icon(
                            painter = rememberVectorPainter(image = arrowBackIcon),
                            contentDescription = "Back",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    onClick = onCloseClick,
                    containerColor = CyanNeon.copy(alpha = 0.2f)
                )
            } else {
                CircleButton(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = CloudWhite,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    onClick = onCloseClick,
                    containerColor = CyanNeon.copy(alpha = 0.5f)
                )
            }

            Button(
                text = if (isEditing) "Save" else "Edit",
                onClick = onEditClick,
                containerColor = if (isEditing) Green.copy(alpha = 0.5f) else CyanNeon.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
fun HeroWalletSectionShimmer() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 0.dp, start = 20.dp, end = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .height(20.dp)
                .width(120.dp)
                .clip(AppCardShape)
                .shimmerEffect()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .height(40.dp)
                .width(180.dp)
                .clip(AppCardShape)
                .shimmerEffect()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .height(40.dp)
                .width(110.dp)
                .clip(AppCardShape)
                .shimmerEffect()
        )
    }
}
