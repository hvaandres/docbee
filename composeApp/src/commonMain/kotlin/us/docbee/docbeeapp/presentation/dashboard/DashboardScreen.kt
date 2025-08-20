package us.docbee.docbeeapp.presentation.dashboard

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import us.docbee.docbeeapp.presentation.components.menus.BottomMenuBar
import us.docbee.docbeeapp.presentation.components.toolbar.Toolbar
import us.docbee.docbeeapp.presentation.components.toolbar.fetchToolbarContent
import us.docbee.docbeeapp.presentation.dashboard.navigation.DashboardNavigation
import us.docbee.docbeeapp.presentation.dashboard.navigation.MenuBarItem
import us.docbee.docbeeapp.presentation.theme.Black

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(parentNavController: NavHostController) {
    val dashboardNavController = rememberNavController()
    var selectedItem by remember { mutableStateOf(MenuBarItem.HOME) }
    val contentToolbar = remember(selectedItem) { fetchToolbarContent(selectedItem) }
    Scaffold(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.navigationBars),
        containerColor = Black,
        topBar = {
            Toolbar(
                title = contentToolbar.title,
                subtitle = contentToolbar.description,
                onBackClicked = { }
            )
        },
        bottomBar = {
            BottomMenuBar(
                selectedItem = selectedItem,
                onItemClick = { item ->
                    selectedItem = item
                    dashboardNavController.navigate(item.route)
                },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    ) { parentPadding ->
        DashboardNavigation(
            modifier = Modifier.padding(parentPadding),
            navController = dashboardNavController,
            parentNavController = dashboardNavController
        )
    }
}
