package us.docbee.docbeeapp.presentation.components.toolbar

import docbee.composeapp.generated.resources.Res
import docbee.composeapp.generated.resources.home_directory_app_bar_subtitle
import docbee.composeapp.generated.resources.home_directory_app_bar_title
import docbee.composeapp.generated.resources.home_emergency_app_bar_subtitle
import docbee.composeapp.generated.resources.home_emergency_app_bar_title
import docbee.composeapp.generated.resources.home_history_app_bar_subtitle
import docbee.composeapp.generated.resources.home_history_app_bar_title
import docbee.composeapp.generated.resources.home_settings_app_bar_subtitle
import docbee.composeapp.generated.resources.home_settings_app_bar_title
import org.jetbrains.compose.resources.StringResource
import us.docbee.docbeeapp.presentation.dashboard.navigation.MenuBarItem

data class ToolbarContent(
    val title: StringResource,
    val description: StringResource
)

fun fetchToolbarContent(item: MenuBarItem): ToolbarContent {
    return when (item) {
        MenuBarItem.HOME -> ToolbarContent(
            title = Res.string.home_emergency_app_bar_title,
            description = Res.string.home_emergency_app_bar_subtitle
        )
        MenuBarItem.DIRECTORY -> ToolbarContent(
            title = Res.string.home_directory_app_bar_title,
            description = Res.string.home_directory_app_bar_subtitle
        )
        MenuBarItem.HISTORY -> ToolbarContent(
            title = Res.string.home_history_app_bar_title,
            description = Res.string.home_history_app_bar_subtitle
        )
        MenuBarItem.SETTINGS -> ToolbarContent(
            title = Res.string.home_settings_app_bar_title,
            description = Res.string.home_settings_app_bar_subtitle
        )
    }
}