package us.docbee.docbeeapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import us.docbee.docbeeapp.presentation.navigation.routes.addDashboardNav
import us.docbee.docbeeapp.presentation.navigation.routes.addLoginNav

@Composable
fun AppNavigator() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = AuthenticationRoute
    ) {
        addLoginNav(navController)
        addDashboardNav(navController)
    }
}