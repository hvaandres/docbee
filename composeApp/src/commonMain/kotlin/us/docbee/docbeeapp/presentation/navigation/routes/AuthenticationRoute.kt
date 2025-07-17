package us.docbee.docbeeapp.presentation.navigation.routes

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import us.docbee.docbeeapp.presentation.login.AuthScreen
import us.docbee.docbeeapp.presentation.navigation.AuthenticationRoute

fun NavGraphBuilder.addLoginNav(navController: NavController) {
    composable<AuthenticationRoute> {
        AuthScreen(navController)
    }
}