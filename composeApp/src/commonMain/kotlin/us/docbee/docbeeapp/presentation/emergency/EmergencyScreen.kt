package us.docbee.docbeeapp.presentation.emergency

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.NavController
import docbee.composeapp.generated.resources.Res
import docbee.composeapp.generated.resources.background_map
import docbee.composeapp.generated.resources.emergency_toolbar_title
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.painterResource
import us.docbee.docbeeapp.presentation.components.toolbar.ToolbarSecondary
import us.docbee.docbeeapp.presentation.emergency.effects.EmergencyEffects
import us.docbee.docbeeapp.presentation.emergency.events.EmergencyEvents
import us.docbee.docbeeapp.presentation.theme.Transparent

@Composable
fun EmergencyScreen(
    navController: NavController,
    viewModel: EmergencyViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is EmergencyEffects.NavigateBack -> navController.navigateUp()
                is EmergencyEffects.NotifyImSafe -> navController.navigateUp()
            }
        }
    }

    Box {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(Res.drawable.background_map),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Scaffold(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.navigationBars),
            containerColor = Transparent,
            topBar = {
                ToolbarSecondary(
                    title = Res.string.emergency_toolbar_title,
                    onBackClicked = { viewModel.onEvent(EmergencyEvents.OnBackPressed) }
                )
            }
        ) { contentPadding ->

        }
    }
}