package us.docbee.docbeeapp.presentation.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import docbee.composeapp.generated.resources.Res
import docbee.composeapp.generated.resources.ic_asterisk
import docbee.composeapp.generated.resources.ic_contact
import docbee.composeapp.generated.resources.ic_location
import docbee.composeapp.generated.resources.ic_logout
import docbee.composeapp.generated.resources.ic_profile
import docbee.composeapp.generated.resources.settings_contacts_description
import docbee.composeapp.generated.resources.settings_contacts_title
import docbee.composeapp.generated.resources.settings_emergency_notification_description
import docbee.composeapp.generated.resources.settings_emergency_notification_title
import docbee.composeapp.generated.resources.settings_location_description
import docbee.composeapp.generated.resources.settings_location_title
import docbee.composeapp.generated.resources.settings_logout_description
import docbee.composeapp.generated.resources.settings_logout_title
import docbee.composeapp.generated.resources.settings_update_profile_description
import docbee.composeapp.generated.resources.settings_update_profile_title
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.stringResource
import us.docbee.docbeeapp.presentation.components.CardDescriptionItem
import us.docbee.docbeeapp.presentation.navigation.AuthenticationRoute
import us.docbee.docbeeapp.presentation.settings.effects.SettingsEffect
import us.docbee.docbeeapp.presentation.settings.events.SettingsEvent

@Composable
fun SettingsScreen(
    parentNavHostController: NavHostController,
    viewModel: SettingsViewModel
) {
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is SettingsEffect.NavigateToLogin -> {
                    parentNavHostController.navigate(AuthenticationRoute) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(vertical = 24.dp, horizontal = 48.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CardDescriptionItem(
            title = stringResource(Res.string.settings_update_profile_title),
            description = stringResource(Res.string.settings_update_profile_description),
            icon = Res.drawable.ic_profile,
            onClick = { }
        )
        CardDescriptionItem(
            title = stringResource(Res.string.settings_contacts_title),
            description = stringResource(Res.string.settings_contacts_description),
            icon = Res.drawable.ic_contact,
            onClick = { }
        )
        CardDescriptionItem(
            title = stringResource(Res.string.settings_emergency_notification_title),
            description = stringResource(Res.string.settings_emergency_notification_description),
            icon = Res.drawable.ic_asterisk,
            onClick = { }
        )
        CardDescriptionItem(
            title = stringResource(Res.string.settings_location_title),
            description = stringResource(Res.string.settings_location_description),
            icon = Res.drawable.ic_location,
            onClick = { }
        )
        CardDescriptionItem(
            title = stringResource(Res.string.settings_logout_title),
            description = stringResource(Res.string.settings_logout_description),
            icon = Res.drawable.ic_logout,
            onClick = { viewModel.onEvent(SettingsEvent.OnClickLogout) }
        )
    }
}