package us.docbee.docbeeapp.presentation.dashboard.states

import us.docbee.docbeeapp.presentation.components.toolbar.ToolbarContent
import us.docbee.docbeeapp.presentation.components.toolbar.fetchToolbarContent
import us.docbee.docbeeapp.presentation.dashboard.navigation.MenuBarItem

data class StateUi(
    val tabItemSelected: MenuBarItem = MenuBarItem.HOME,
    val toolbarSelected: ToolbarContent = fetchToolbarContent(MenuBarItem.HOME)
)