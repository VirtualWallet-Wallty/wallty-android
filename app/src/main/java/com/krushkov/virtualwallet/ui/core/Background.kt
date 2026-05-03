package com.krushkov.virtualwallet.ui.core

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import com.krushkov.virtualwallet.ui.theme.*

@Composable
fun Background(
    modifier: Modifier = Modifier,
    style: AppBackgroundStyle = AppBackgroundStyle.Signature
) {
    when (style) {
        is AppBackgroundStyle.Signature -> SignatureBackground(modifier)
        is AppBackgroundStyle.Solid -> SolidBackground(modifier, style.color)
    }
}

sealed interface AppBackgroundStyle {
    data object Signature : AppBackgroundStyle
    data class Solid(val color: Color = Black) : AppBackgroundStyle
}

@Composable
private fun SolidBackground(
    modifier: Modifier = Modifier,
    color: Color = Black
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color)
    )
}

@Composable
private fun SignatureBackground(modifier: Modifier = Modifier) {
    val glowBlur = 60.dp
    val electricBlur = 30.dp
    val cyanMaxBlur = 40.dp
    val waveBlur = 30.dp

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Black)
    ) {
        // Layer 1: Background Glows (High Blur)
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .blur(glowBlur)
        ) {
            val scale = size.width / 412f

            // Blue Glows
            rotate(35.58f, pivot = Offset(-61f * scale, 608f * scale)) {
                drawRoundRect(
                    color = ElectricBlue.copy(alpha = 0.2f),
                    topLeft = Offset(-61f * scale, 608f * scale),
                    size = Size(532f * scale, 216f * scale),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(108f * scale)
                )
            }
            rotate(-41.84f, pivot = Offset(-87f * scale, 815f * scale)) {
                drawRoundRect(
                    color = ElectricBlue.copy(alpha = 0.2f),
                    topLeft = Offset(-87f * scale, 815f * scale),
                    size = Size(486f * scale, 432f * scale),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(108f * scale)
                )
            }

            // Cyan Glows
            rotate(-11.91f, pivot = Offset(-230f * scale, 48f * scale)) {
                drawRoundRect(
                    color = CyanNeon.copy(alpha = 0.2f),
                    topLeft = Offset(-230f * scale, 48f * scale),
                    size = Size(216f * scale, 994f * scale),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(108f * scale)
                )
            }
            rotate(6.03f, pivot = Offset(339f * scale, 27f * scale)) {
                drawRoundRect(
                    color = CyanNeon.copy(alpha = 0.2f),
                    topLeft = Offset(339f * scale, 27f * scale),
                    size = Size(157f * scale, 1009f * scale),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(78f * scale)
                )
            }
        }

        // Layer 2: ElectricBlue Ellipse - BLURRED (Behind)
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .blur(electricBlur)
        ) {
            val scale = size.width / 412f
            drawOval(
                brush = Brush.verticalGradient(
                    0.0f to ElectricBlue.copy(alpha = 1f),
                    0.5f to ElectricBlue.copy(alpha = 0.1f),
                    startY = 132f * scale,
                    endY = 461f * scale
                ),
                topLeft = Offset((205f - 301f) * scale, (333f - 204f) * scale),
                size = Size(602f * scale, 408f * scale)
            )
        }

        // Layer 3: CyanNeon Ellipse - CRISP COMPONENT
        Canvas(modifier = Modifier.fillMaxSize()) {
            val scale = size.width / 412f
            drawOval(
                brush = Brush.verticalGradient(
                    0.0f to CyanNeon.copy(alpha = 0.7f),
                    0.5f to CyanNeon.copy(alpha = 0f),
                    startY = 132f * scale,
                    endY = 461f * scale
                ),
                topLeft = Offset((205f - 274f) * scale, (296f - 164f) * scale),
                size = Size(548f * scale, 328f * scale)
            )
        }

        // Layer 4: CyanNeon Ellipse - BLURRED BODY
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .blur(cyanMaxBlur)
        ) {
            val scale = size.width / 412f
            drawOval(
                brush = Brush.verticalGradient(
                    0.0f to CyanNeon.copy(alpha = 0f),
                    0.5f to CyanNeon.copy(alpha = 0.1f),
                    startY = 132f * scale,
                    endY = 461f * scale
                ),
                topLeft = Offset((205f - 274f) * scale, (296f - 164f) * scale),
                size = Size(548f * scale, 328f * scale)
            )
        }

        // Layer 5: Top Waves (Specific Blur)
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .blur(waveBlur)
        ) {
            val scale = size.width / 412f

            // Draw Big Wave (CyanNeon) first
            val path7 = Path().apply {
                moveTo(-2f * scale, 62f * scale)
                cubicTo(108f * scale, 40f * scale, 169f * scale, 129f * scale, 262f * scale, 134f * scale)
                cubicTo(355f * scale, 140f * scale, 340f * scale, 123f * scale, 412f * scale, 123f * scale)
                lineTo(412f * scale, -1f * scale)
                lineTo(-2f * scale, -1f * scale)
                close()
            }
            drawPath(path = path7, color = CyanNeon.copy(alpha = 0.7f))

            // Draw Small Wave (ElectricBlue) on top
            val path8 = Path().apply {
                moveTo(-11f * scale, 26f * scale)
                cubicTo(68f * scale, 11f * scale, 131f * scale, 25f * scale, 207f * scale, 52f * scale)
                cubicTo(286f * scale, 80f * scale, 346f * scale, 95f * scale, 421f * scale, 95f * scale)
                lineTo(421f * scale, -7f * scale)
                lineTo(-11f * scale, -7f * scale)
                close()
            }
            drawPath(path = path8, color = ElectricBlue.copy(alpha = 0.7f))
        }

        // Edge Gradients (Top for Status Bar, Bottom for Gesture Bar)
        Canvas(modifier = Modifier.fillMaxSize()) {
            val topFadeHeight = 120.dp.toPx()
            val bottomFadeHeight = 160.dp.toPx()
            
            // Top Edge Gradient (Fluent Black fade)
            drawRect(
                brush = Brush.verticalGradient(
                    0.0f to Black,
                    0.3f to Black.copy(alpha = 0.8f),
                    0.6f to Black.copy(alpha = 0.4f),
                    1.0f to Color.Transparent,
                    startY = 0f,
                    endY = topFadeHeight
                ),
                size = Size(size.width, topFadeHeight)
            )

            // Bottom Edge Gradient (Fluent Black fade)
            drawRect(
                brush = Brush.verticalGradient(
                    0.0f to Color.Transparent,
                    0.4f to Black.copy(alpha = 0.4f),
                    0.7f to Black.copy(alpha = 0.8f),
                    1.0f to Black,
                    startY = size.height - bottomFadeHeight,
                    endY = size.height
                ),
                topLeft = Offset(0f, size.height - bottomFadeHeight),
                size = Size(size.width, bottomFadeHeight)
            )
        }
    }
}
