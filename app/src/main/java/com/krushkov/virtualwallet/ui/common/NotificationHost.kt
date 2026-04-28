package com.krushkov.virtualwallet.ui.common

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krushkov.virtualwallet.ui.utils.outerShadow
import com.krushkov.virtualwallet.ui.theme.*
import kotlinx.coroutines.delay

data class NotificationData(
    val message: String,
    val isSuccess: Boolean,
    val id: Long = System.currentTimeMillis()
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppNotificationHost(
    notification: NotificationData?,
    onDismiss: () -> Unit
) {
    val notificationShape = AppCardShape

    val successIcon = remember {
        ImageVector.Builder(
            name = "SuccessIcon",
            defaultWidth = 32.dp,
            defaultHeight = 32.dp,
            viewportWidth = 32f,
            viewportHeight = 32f
        ).path(
            stroke = SolidColor(Color(0xFF42B238)),
            strokeLineWidth = 2f
        ) {
            moveTo(16f, 1f)
            arcTo(15f, 15f, 0f, true, true, 15.999f, 1f)
            close()
        }.path(
            stroke = SolidColor(Color(0xFF42B238)),
            strokeLineWidth = 2.5f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round
        ) {
            moveTo(24f, 10f)
            lineTo(13f, 21f)
            lineTo(8f, 16f)
        }.build()
    }

    val errorIcon = remember {
        ImageVector.Builder(
            name = "ErrorIcon",
            defaultWidth = 32.dp,
            defaultHeight = 32.dp,
            viewportWidth = 32f,
            viewportHeight = 32f
        ).path(
            stroke = SolidColor(Color(0xFFCD2B2B)),
            strokeLineWidth = 2f
        ) {
            moveTo(16f, 1f)
            arcTo(15f, 15f, 0f, true, true, 15.999f, 1f)
            close()
        }.path(
            fill = SolidColor(Color(0xFFCD2B2B))
        ) {
            moveTo(17.1657f, 8.45455f)
            lineTo(16.981f, 18.8168f)
            horizontalLineTo(15.0137f)
            lineTo(14.829f, 8.45455f)
            horizontalLineTo(17.1657f)
            close()
            moveTo(16.0009f, 23.1349f)
            curveTo(15.6079f, 23.1349f, 15.2717f, 22.9976f, 14.9924f, 22.723f)
            curveTo(14.713f, 22.4437f, 14.5757f, 22.1075f, 14.5804f, 21.7145f)
            curveTo(14.5757f, 21.3262f, 14.713f, 20.9948f, 14.9924f, 20.7202f)
            curveTo(15.2717f, 20.4408f, 15.6079f, 20.3011f, 16.0009f, 20.3011f)
            curveTo(16.3844f, 20.3011f, 16.7158f, 20.4408f, 16.9952f, 20.7202f)
            curveTo(17.2746f, 20.9948f, 17.4166f, 21.3262f, 17.4213f, 21.7145f)
            curveTo(17.4166f, 21.9749f, 17.348f, 22.214f, 17.2154f, 22.4318f)
            curveTo(17.0875f, 22.6449f, 16.9171f, 22.8153f, 16.704f, 22.9432f)
            curveTo(16.4909f, 23.071f, 16.2566f, 23.1349f, 16.0009f, 23.1349f)
            close()
        }.build()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        contentAlignment = Alignment.TopCenter
    ) {
        AnimatedVisibility(
            visible = notification != null,
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut()
        ) {
            notification?.let { data ->
                LaunchedEffect(data.id) {
                    delay(4000)
                    onDismiss()
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .outerShadow(notificationShape)
                        .clip(notificationShape)
                        .border(AppBorderStroke, notificationShape)
                ) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(Black.copy(alpha = 0.8f))
                    )

                    // Colored Overlay Layer
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(
                                if (data.isSuccess) Green.copy(alpha = 0.5f) else Red.copy(alpha = 0.5f)
                            )
                    )

                    // Content Layer
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            painter = rememberVectorPainter(image = if (data.isSuccess) successIcon else errorIcon),
                            contentDescription = if (data.isSuccess) "Success" else "Error",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(24.dp)
                        )

                        Text(
                            text = data.message,
                            color = CloudWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1,
                            modifier = Modifier
                                .weight(1f)
                                .basicMarquee(
                                    iterations = Int.MAX_VALUE,
                                    delayMillis = 2000
                                )
                        )
                    }
                }
            }
        }
    }
}
