package us.docbee.docbeeapp.presentation.dashboard

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.collectLatest
import us.docbee.docbeeapp.presentation.components.menus.BottomMenuBar
import us.docbee.docbeeapp.presentation.components.toolbar.Toolbar
import us.docbee.docbeeapp.presentation.dashboard.effects.DashboardEffects
import us.docbee.docbeeapp.presentation.dashboard.events.DashboardEvents
import us.docbee.docbeeapp.presentation.dashboard.navigation.DashboardNavigation
import us.docbee.docbeeapp.presentation.dashboard.navigation.DashboardRoutes
import us.docbee.docbeeapp.presentation.dashboard.navigation.MenuBarItem
import us.docbee.docbeeapp.presentation.theme.Black
import us.docbee.docbeeapp.utils.ui.SetStatusBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    parentNavController: NavHostController,
    dashboardViewModel: DashboardViewModel
) {
    val dashboardNavController = rememberNavController()
    val uiState by dashboardViewModel.uiState.collectAsState()

    DisposableEffect(dashboardNavController) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            when (destination.route) {
                DashboardRoutes.HomeRoute::class.qualifiedName ->
                    dashboardViewModel.onEvent(DashboardEvents.OnUpdateTab(MenuBarItem.HOME))

                DashboardRoutes.DirectoryRoute::class.qualifiedName ->
                    dashboardViewModel.onEvent(DashboardEvents.OnUpdateTab(MenuBarItem.DIRECTORY))

                DashboardRoutes.AlertsRoute::class.qualifiedName ->
                    dashboardViewModel.onEvent(DashboardEvents.OnUpdateTab(MenuBarItem.ALERTS))

                DashboardRoutes.SettingsRoute::class.qualifiedName ->
                    dashboardViewModel.onEvent(DashboardEvents.OnUpdateTab(MenuBarItem.SETTINGS))
            }
        }
        dashboardNavController.addOnDestinationChangedListener(listener)
        onDispose {
            dashboardNavController.removeOnDestinationChangedListener(listener)
        }
    }

    LaunchedEffect(Unit) {
        dashboardViewModel.effect.collectLatest { effect ->
            when (effect) {
                is DashboardEffects.NavigateNewTab -> {
                    dashboardNavController.navigate(effect.route) {
                        popUpTo(DashboardRoutes.HomeRoute) { inclusive = false }
                        launchSingleTop = true
                    }
                }
                is DashboardEffects.NavigateBack -> dashboardNavController.navigateUp()
            }
        }
    }
    SetStatusBar(isDarkMode = true)
    Scaffold(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.navigationBars),
        containerColor = Black,
        topBar = {
            Toolbar(
                title = uiState.toolbarSelected.title,
                subtitle = uiState.toolbarSelected.description,
                isBackVisible = uiState.tabItemSelected != MenuBarItem.HOME,
                onBackClicked = { dashboardViewModel.onEvent(DashboardEvents.OnBackClicked) }
            )
        },
        bottomBar = {
            BottomMenuBar(
                selectedItem = uiState.tabItemSelected,
                onItemClick = { item ->
                    dashboardViewModel.onEvent(DashboardEvents.OnTabSelected(item))
                },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    ) { parentPadding ->
        DashboardNavigation(
            modifier = Modifier.padding(parentPadding),
            navController = dashboardNavController,
            parentNavController = parentNavController
        )
    }
}
