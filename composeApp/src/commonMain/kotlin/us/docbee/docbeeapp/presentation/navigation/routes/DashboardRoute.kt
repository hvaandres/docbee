package us.docbee.docbeeapp.presentation.navigation.routes

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import org.koin.compose.viewmodel.koinViewModel
import us.docbee.docbeeapp.presentation.dashboard.DashboardScreen
import us.docbee.docbeeapp.presentation.navigation.DashboardRoute

fun NavGraphBuilder.addDashboardNav(navController: NavHostController) {
    composable<DashboardRoute> {
        DashboardScreen(navController, koinViewModel())
    }
}