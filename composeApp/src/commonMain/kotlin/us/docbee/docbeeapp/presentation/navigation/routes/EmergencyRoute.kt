package us.docbee.docbeeapp.presentation.navigation.routes

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.koin.compose.viewmodel.koinViewModel
import us.docbee.docbeeapp.presentation.emergency.EmergencyScreen
import us.docbee.docbeeapp.presentation.emergency.EmergencyViewModel
import us.docbee.docbeeapp.presentation.navigation.EmergencyRoute

fun NavGraphBuilder.addEmergencyNav(navController: NavController) {
    composable<EmergencyRoute> {
        EmergencyScreen(navController = navController, koinViewModel<EmergencyViewModel>())
    }
}