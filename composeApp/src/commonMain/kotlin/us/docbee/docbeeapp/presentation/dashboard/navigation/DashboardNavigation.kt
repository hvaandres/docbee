package us.docbee.docbeeapp.presentation.dashboard.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import us.docbee.docbeeapp.presentation.dashboard.navigation.routes.addDirectoryNav
import us.docbee.docbeeapp.presentation.dashboard.navigation.routes.addHistoryNav
import us.docbee.docbeeapp.presentation.dashboard.navigation.routes.addHomeNav
import us.docbee.docbeeapp.presentation.dashboard.navigation.routes.addSettingsNav

@Composable
fun DashboardNavigation(
    navController: NavHostController,
    parentNavController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = DashboardRoutes.HomeRoute,
        modifier = modifier
    ) {
        addHomeNav(parentNavController = parentNavController, navController = navController)
        addDirectoryNav(parentNavController = parentNavController, navController = navController)
        addSettingsNav(parentNavController)
        addHistoryNav(parentNavController = parentNavController, navController = navController)
    }
}