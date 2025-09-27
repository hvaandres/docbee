package us.docbee.docbeeapp.presentation.navigation.routes

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.koin.compose.viewmodel.koinViewModel
import us.docbee.docbeeapp.presentation.navigation.SplashRoute
import us.docbee.docbeeapp.presentation.splash.SplashScreen

fun NavGraphBuilder.addSplashNav(navController: NavController) {
    composable<SplashRoute> {
        SplashScreen(navController = navController, viewModel = koinViewModel())
    }
}