package us.docbee.docbeeapp.presentation.dashboard.navigation.routes

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import us.docbee.docbeeapp.presentation.dashboard.navigation.DashboardRoutes
import us.docbee.docbeeapp.presentation.settings.SettingsScreen

fun NavGraphBuilder.addSettingsNav(navHostController: NavHostController) {
    composable<DashboardRoutes.SettingsRoute> {
        SettingsScreen(navController = navHostController)
    }
}