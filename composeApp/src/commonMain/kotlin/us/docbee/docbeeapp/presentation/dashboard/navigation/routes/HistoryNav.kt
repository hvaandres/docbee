package us.docbee.docbeeapp.presentation.dashboard.navigation.routes

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import org.koin.compose.viewmodel.koinViewModel
import us.docbee.docbeeapp.presentation.dashboard.navigation.DashboardRoutes
import us.docbee.docbeeapp.presentation.alerts.AlertsScreen

fun NavGraphBuilder.addHistoryNav(
    parentNavController: NavHostController,
    navController: NavHostController
) {
    composable<DashboardRoutes.AlertsRoute> {
        AlertsScreen(
            parentNavController = parentNavController,
            navController = navController,
            viewModel = koinViewModel()
        )
    }
}