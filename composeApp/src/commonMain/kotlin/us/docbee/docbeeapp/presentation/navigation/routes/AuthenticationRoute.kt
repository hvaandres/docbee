package us.docbee.docbeeapp.presentation.navigation.routes

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.koin.compose.viewmodel.koinViewModel
import us.docbee.docbeeapp.presentation.login.AuthScreen
import us.docbee.docbeeapp.presentation.navigation.AuthenticationRoute

fun NavGraphBuilder.addLoginNav(navController: NavController) {
    composable<AuthenticationRoute> {
        AuthScreen(
            navController = navController,
            viewModel = koinViewModel()
        )
    }
}