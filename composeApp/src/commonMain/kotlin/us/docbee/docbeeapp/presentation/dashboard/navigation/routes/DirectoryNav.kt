package us.docbee.docbeeapp.presentation.dashboard.navigation.routes

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import us.docbee.docbeeapp.presentation.dashboard.navigation.DashboardRoutes
import us.docbee.docbeeapp.presentation.directory.DirectoryScreen

fun NavGraphBuilder.addDirectoryNav(navHostController: NavHostController) {
    composable<DashboardRoutes.DirectoryRoute> {
        DirectoryScreen(navController = navHostController)
    }
}