package us.docbee.docbeeapp.presentation.navigation.routes

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import us.docbee.docbeeapp.presentation.alerts.AlertsDetailScreen
import us.docbee.docbeeapp.presentation.navigation.AlertDetailRoute

fun NavGraphBuilder.addAlertsDetailNav(navController: NavHostController) {
    composable<AlertDetailRoute> {
        AlertsDetailScreen(navController = navController)
    }
}