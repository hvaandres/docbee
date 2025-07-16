package us.docbee.docbeeapp.presentation.dashboard.navigation

import kotlinx.serialization.Serializable


sealed class DashboardRoutes {
    @Serializable
    data object HomeRoute: DashboardRoutes()

    @Serializable
    data object DirectoryRoute: DashboardRoutes()

    @Serializable
    data object HistoryRoute: DashboardRoutes()

    @Serializable
    data object SettingsRoute: DashboardRoutes()
}