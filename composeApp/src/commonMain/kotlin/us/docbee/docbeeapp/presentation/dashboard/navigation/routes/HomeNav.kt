package us.docbee.docbeeapp.presentation.dashboard.navigation.routes

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import us.docbee.docbeeapp.presentation.dashboard.navigation.DashboardRoutes
import us.docbee.docbeeapp.presentation.home.HomeScreen

fun NavGraphBuilder.addHomeNav(navHostController: NavHostController) {
    composable<DashboardRoutes.HomeRoute> {
        HomeScreen(navController = navHostController)
    }
}