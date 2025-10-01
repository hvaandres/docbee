package us.docbee.docbeeapp.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import docbee.composeapp.generated.resources.Res
import docbee.composeapp.generated.resources.home_emergency_button_description
import docbee.composeapp.generated.resources.home_emergency_button_title
import docbee.composeapp.generated.resources.home_emergency_permission_required_button_accept
import docbee.composeapp.generated.resources.home_emergency_permission_required_button_deny
import docbee.composeapp.generated.resources.home_emergency_permission_required_description
import docbee.composeapp.generated.resources.home_emergency_permission_required_title
import docbee.composeapp.generated.resources.ic_emergency_button
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import us.docbee.docbeeapp.presentation.components.PrimaryButton
import us.docbee.docbeeapp.presentation.home.effects.HomeEffects
import us.docbee.docbeeapp.presentation.home.events.HomeEvents
import us.docbee.docbeeapp.presentation.navigation.EmergencyRoute
import us.docbee.docbeeapp.presentation.theme.Green100
import us.docbee.docbeeapp.presentation.theme.White
import us.docbee.docbeeapp.presentation.theme.white100
import us.docbee.docbeeapp.utils.ui.permissions.providePermissionSettingsManager

@Composable
fun HomeScreen(
    parentNavController: NavHostController,
    viewModel: HomeViewModel
) {

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is HomeEffects.NavigateToEmergency -> parentNavController.navigate(EmergencyRoute)
                is HomeEffects.NavigateToSettings -> providePermissionSettingsManager().openAppSettings()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        HomeScreenContent(onEmergencyClick = { viewModel.onEvent(HomeEvents.OnClickEmergency) })

        if (uiState.shouldShowPermissionRequestModal) {
            AlertDialog(
                modifier = Modifier,
                onDismissRequest = { viewModel.onEvent(HomeEvents.OnDismissSettings) },
                title = {
                    Text(text = stringResource(Res.string.home_emergency_permission_required_title))
                },
                text = {
                    Text(text = stringResource(Res.string.home_emergency_permission_required_description))
                },
                confirmButton = {
                    PrimaryButton(
                        modifier = Modifier.wrapContentWidth(),
                        text = stringResource(Res.string.home_emergency_permission_required_button_accept),
                        onClick = { viewModel.onEvent(HomeEvents.OnOpenSettings) }
                    )
                },
                dismissButton = {
                    PrimaryButton(
                        text = stringResource(Res.string.home_emergency_permission_required_button_deny),
                        onClick = { viewModel.onEvent(HomeEvents.OnDismissSettings) }
                    )
                }
            )
        }
    }
}

@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    onEmergencyClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.size(300.dp)
                .clip(CircleShape)
                .clickable { onEmergencyClick() },
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
                    .align(Alignment.Center)
                    .background(color = Green100, shape = CircleShape)
            )
            Box(
                modifier = Modifier.padding(50.dp)
                    .fillMaxSize()
                    .align(Alignment.Center)
                    .background(color = white100, shape = CircleShape)
            )
            Image(
                modifier = Modifier.align(Alignment.Center),
                imageVector = vectorResource(Res.drawable.ic_emergency_button),
                contentDescription = null
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(Res.string.home_emergency_button_title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = White,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            modifier = Modifier.padding(horizontal = 64.dp),
            text = stringResource(Res.string.home_emergency_button_description),
            style = MaterialTheme.typography.bodySmall,
            color = White,
            textAlign = TextAlign.Center
        )
    }
}