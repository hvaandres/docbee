package us.docbee.docbeeapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import us.docbee.docbeeapp.presentation.navigation.routes.addContactNav
import us.docbee.docbeeapp.presentation.navigation.routes.addDashboardNav
import us.docbee.docbeeapp.presentation.navigation.routes.addEmergencyNav
import us.docbee.docbeeapp.presentation.navigation.routes.addLoginNav
import us.docbee.docbeeapp.presentation.navigation.routes.addSplashNav

@Composable
fun AppNavigator() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = SplashRoute
    ) {
        addSplashNav(navController)
        addLoginNav(navController)
        addDashboardNav(navController)
        addContactNav(navController)
        addEmergencyNav(navController)
    }
}