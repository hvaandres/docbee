package us.docbee.docbeeapp.presentation.dashboard.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import docbee.composeapp.generated.resources.Res
import docbee.composeapp.generated.resources.dashboard_directory_title
import docbee.composeapp.generated.resources.dashboard_history_title
import docbee.composeapp.generated.resources.dashboard_home_title
import docbee.composeapp.generated.resources.dashboard_settings_title
import org.jetbrains.compose.resources.StringResource

enum class MenuBarItem(
    val icon: ImageVector,
    val iconLinear: ImageVector,
    val label: StringResource,
    val route: DashboardRoutes
) {
    HOME(
        Icons.Default.Home,
        Icons.Outlined.Home,
        Res.string.dashboard_home_title,
        DashboardRoutes.HomeRoute
    ),
    DIRECTORY(
        Icons.AutoMirrored.Filled.List,
        Icons.AutoMirrored.Outlined.List,
        Res.string.dashboard_directory_title,
        DashboardRoutes.DirectoryRoute
    ),
    HISTORY(
        Icons.Default.Info,
        Icons.Outlined.Info,
        Res.string.dashboard_history_title,
        DashboardRoutes.HistoryRoute
    ),
    SETTINGS(
        Icons.Default.Settings,
        Icons.Outlined.Settings,
        Res.string.dashboard_settings_title,
        DashboardRoutes.SettingsRoute
    )
}