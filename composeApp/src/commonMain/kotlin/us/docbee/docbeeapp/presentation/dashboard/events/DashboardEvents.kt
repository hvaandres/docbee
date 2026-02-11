package us.docbee.docbeeapp.presentation.dashboard.events

import us.docbee.docbeeapp.presentation.dashboard.navigation.MenuBarItem

sealed class DashboardEvents {
    data object OnRegisterNetworkMonitor: DashboardEvents()
    data class OnTabSelected(val tab: MenuBarItem): DashboardEvents()
    data class OnUpdateTab(val tab: MenuBarItem): DashboardEvents()
    data object OnBackClicked: DashboardEvents()
}
