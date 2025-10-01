package us.docbee.docbeeapp.presentation.dashboard.navigation.routes

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import org.koin.compose.viewmodel.koinViewModel
import us.docbee.docbeeapp.presentation.dashboard.navigation.DashboardRoutes
import us.docbee.docbeeapp.presentation.home.HomeScreen

fun NavGraphBuilder.addHomeNav(parentNavController: NavHostController) {
    composable<DashboardRoutes.HomeRoute> {
        HomeScreen(
            parentNavController = parentNavController,
            viewModel = koinViewModel()
        )
    }
}