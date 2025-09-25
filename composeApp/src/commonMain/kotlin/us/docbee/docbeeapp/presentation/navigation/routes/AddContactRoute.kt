package us.docbee.docbeeapp.presentation.navigation.routes

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import org.koin.compose.viewmodel.koinViewModel
import us.docbee.docbeeapp.presentation.directory.AddContactScreen
import us.docbee.docbeeapp.presentation.navigation.AddContactRoute

fun NavGraphBuilder.addContactNav(navController: NavHostController) {
    composable<AddContactRoute> {
        AddContactScreen(navController = navController, viewModel = koinViewModel())
    }
}