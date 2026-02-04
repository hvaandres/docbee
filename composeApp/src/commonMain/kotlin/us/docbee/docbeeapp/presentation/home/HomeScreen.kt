package us.docbee.docbeeapp.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import docbee.composeapp.generated.resources.Res
import docbee.composeapp.generated.resources.home_emergency_button_description
import docbee.composeapp.generated.resources.home_emergency_button_title
import docbee.composeapp.generated.resources.home_emergency_contact_required_confirm
import docbee.composeapp.generated.resources.home_emergency_contact_required_description
import docbee.composeapp.generated.resources.home_emergency_permission_required_button_accept
import docbee.composeapp.generated.resources.home_emergency_permission_required_description
import docbee.composeapp.generated.resources.ic_emergency_button
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import us.docbee.docbeeapp.presentation.components.AnimatedVector
import us.docbee.docbeeapp.presentation.components.PrimaryButton
import us.docbee.docbeeapp.presentation.dashboard.navigation.DashboardRoutes
import us.docbee.docbeeapp.presentation.home.effects.HomeEffects
import us.docbee.docbeeapp.presentation.home.events.HomeEvents
import us.docbee.docbeeapp.presentation.home.states.UiState
import us.docbee.docbeeapp.presentation.navigation.EmergencyRoute
import us.docbee.docbeeapp.presentation.screen.NoInternetConnectionScreen
import us.docbee.docbeeapp.presentation.theme.Green100
import us.docbee.docbeeapp.presentation.theme.White
import us.docbee.docbeeapp.presentation.theme.white100
import us.docbee.docbeeapp.utils.EMPTY_STATE_ANIMATED_VECTOR
import us.docbee.docbeeapp.utils.LOCATION_PERMISSION_VECTOR
import us.docbee.docbeeapp.utils.ui.permissions.providePermissionSettingsManager

@Composable
fun HomeScreen(
    parentNavController: NavHostController,
    navController: NavHostController,
    viewModel: HomeViewModel
) {

    val uiState by viewModel.uiState.collectAsState()

    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        var wasStopped = false
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_STOP -> wasStopped = true
                Lifecycle.Event.ON_START ->  {
                    if (wasStopped) {
                        wasStopped = false
                        viewModel.onEvent(HomeEvents.OnValidateRequirements)
                    }
                }

                Lifecycle.Event.ON_CREATE -> viewModel.onEvent(HomeEvents.OnValidateRequirements)
                else -> Unit
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is HomeEffects.NavigateToEmergency -> parentNavController.navigate(EmergencyRoute)
                is HomeEffects.NavigateToSettings -> providePermissionSettingsManager().openAppSettings()
                is HomeEffects.NavigateToDirectory -> navController.navigate(DashboardRoutes.DirectoryRoute)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            uiState.isLoading -> {
                LoadingScreen()
            }
            uiState.hasNoConnection -> {
                NoInternetConnectionScreen()
            }
            uiState.isPermissionNotGranted -> {
                HomeScreenNoPermission(
                    onSettingsClick = { viewModel.onEvent(HomeEvents.OnOpenSettings) }
                )
            }
            uiState.showContactMissing -> {
                HomeNoContacts(
                    onDirectoryClick = { viewModel.onEvent(HomeEvents.OnClickContacts) }
                )
            }
            else -> {
                HomeScreenContent(
                    emergencyRemainingClicks = uiState.emergencyRemainingClicks,
                    onEmergencyClick = { viewModel.onEvent(HomeEvents.OnClickEmergency) }
                )
            }
        }
    }
}

@Composable
fun HomeScreenNoPermission(
    modifier: Modifier = Modifier,
    onSettingsClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AnimatedVector(
            modifier = Modifier.padding(horizontal = 48.dp).fillMaxWidth(),
            location = LOCATION_PERMISSION_VECTOR
        )
        Text(
            modifier = Modifier.padding(24.dp),
            text = stringResource(Res.string.home_emergency_permission_required_description),
            style = MaterialTheme.typography.bodySmall,
            color = White,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        PrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(Res.string.home_emergency_permission_required_button_accept),
            backgroundColor = Green100,
            onClick = onSettingsClick
        )
    }
}

@Composable
fun HomeNoContacts(
    modifier: Modifier = Modifier,
    onDirectoryClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AnimatedVector(
            modifier = Modifier.padding(horizontal = 48.dp).fillMaxWidth(),
            location = EMPTY_STATE_ANIMATED_VECTOR
        )
        Text(
            modifier = Modifier.padding(24.dp),
            text = stringResource(Res.string.home_emergency_contact_required_description),
            style = MaterialTheme.typography.bodySmall,
            color = White,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        PrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(Res.string.home_emergency_contact_required_confirm),
            backgroundColor = Green100,
            onClick = onDirectoryClick
        )
    }
}

@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    emergencyRemainingClicks: Int,
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
            modifier = Modifier.padding(24.dp)
                .fillMaxWidth()
                .aspectRatio(ratio = 1f)
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
            text = stringResource(Res.string.home_emergency_button_title, emergencyRemainingClicks),
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

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}
