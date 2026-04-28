package com.krushkov.virtualwallet.ui.theme

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import com.krushkov.virtualwallet.ui.core.Background
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val ColorScheme = darkColorScheme(
    primary = CyanNeon,
    secondary = ElectricBlue,
    background = Color.Transparent,
    surface = NightBlack,
    onPrimary = Black,
    onSecondary = CloudWhite,
    onBackground = CloudWhite,
    onSurface = CloudWhite,
    error = Red
)

@Composable
fun VirtualWalletTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = ColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            WindowCompat.setDecorFitsSystemWindows(window, false)
            val controller = WindowCompat.getInsetsController(window, view)
            controller.isAppearanceLightStatusBars = false
            controller.isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography
    ) {
        Box(modifier = androidx.compose.ui.Modifier.fillMaxSize()) {
            Background()
            Box(modifier = androidx.compose.ui.Modifier.fillMaxSize()) {
                content()
            }
        }
    }
}
