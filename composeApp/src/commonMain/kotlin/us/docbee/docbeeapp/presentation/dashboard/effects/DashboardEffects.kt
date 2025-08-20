package us.docbee.docbeeapp.presentation.dashboard.effects

import us.docbee.docbeeapp.presentation.dashboard.navigation.DashboardRoutes

sealed class DashboardEffects {
    data object NavigateBack: DashboardEffects()
    data class NavigateNewTab(val route: DashboardRoutes): DashboardEffects()
}