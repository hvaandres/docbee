package us.docbee.docbeeapp.presentation.navigation.routes

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import us.docbee.docbeeapp.presentation.dashboard.DashboardScreen
import us.docbee.docbeeapp.presentation.navigation.DashboardRoute

fun NavGraphBuilder.addDashboardNav(navController: NavHostController) {
    composable<DashboardRoute> {
        DashboardScreen(navController)
    }
}