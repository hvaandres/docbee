package us.docbee.docbeeapp.presentation.dashboard.navigation.routes

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import us.docbee.docbeeapp.presentation.dashboard.navigation.DashboardRoutes
import us.docbee.docbeeapp.presentation.history.HistoryScreen

fun NavGraphBuilder.addHistoryNav(navHostController: NavHostController) {
    composable<DashboardRoutes.HistoryRoute> {
        HistoryScreen(navController = navHostController)
    }
}