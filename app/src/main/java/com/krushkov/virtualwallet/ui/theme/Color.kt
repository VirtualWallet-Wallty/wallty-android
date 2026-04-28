package com.krushkov.virtualwallet.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val CyanNeon = Color(0xFF00CEC9)
val ElectricBlue = Color(0xFF0984E3)
val NightBlack = Color(0xFF1E272E)
val CloudWhite = Color(0xFFF5F5F5)
val Black = Color(0xFF101010)
val Green = Color(0xFF42B238)
val Red = Color(0xFFCD2B2B)
val Yellow = Color(0xFFDDB61A)
val Orange = Color(0xFFE67E22)

val OutlineGradient = Brush.linearGradient(
    0.0f to CloudWhite,
    0.25f to CloudWhite.copy(alpha = 0f),
    0.75f to CloudWhite,
    1.0f to CloudWhite.copy(alpha = 0f)
)

val AppBorderStroke = BorderStroke(0.5.dp, OutlineGradient)
