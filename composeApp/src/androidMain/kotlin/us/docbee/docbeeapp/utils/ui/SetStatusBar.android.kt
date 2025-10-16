package us.docbee.docbeeapp.utils.ui

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.DialogWindowProvider
import androidx.core.view.WindowCompat

@Composable
actual fun SetStatusBar(isDarkMode: Boolean) {
    val view = LocalView.current
    val window = (view.context as? Activity)?.window ?: return

    SideEffect {
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isDarkMode
    }
}

@Composable
actual fun SetModalStatusBar(isDarkMode: Boolean) {
    val view = LocalView.current
    val window = (view.parent as? DialogWindowProvider)?.window ?: return

    SideEffect {
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isDarkMode
    }
}