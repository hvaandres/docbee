package us.docbee.docbeeapp.presentation.alerts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.collectLatest
import us.docbee.docbeeapp.presentation.alerts.effects.AlertsEffects
import us.docbee.docbeeapp.presentation.alerts.events.AlertsEvents
import us.docbee.docbeeapp.presentation.components.CardDescriptionItem
import us.docbee.docbeeapp.presentation.components.FloatingButton
import us.docbee.docbeeapp.utils.ui.getDrawable

@Composable
fun AlertsScreen(
    navController: NavHostController,
    viewModel: AlertsViewModel
) {

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is AlertsEffects.NavigateToAddAlert -> Unit // TODO: Open Bottom Sheet to Add Alert
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(vertical = 24.dp, horizontal = 48.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(uiState.alerts, key = { it.uid }) { alert ->
                    CardDescriptionItem(
                        title = alert.title,
                        description = alert.description,
                        icon = alert.icon.getDrawable(),
                        onClick = { viewModel.onEvent(AlertsEvents.OnClickAlert(alert.uid)) }
                    )
                }
            }
        }
        FloatingButton(
            modifier = Modifier.align(Alignment.BottomEnd),
            onClick = { viewModel.onEvent(AlertsEvents.OnClickAddAlert) }
        )
    }
}