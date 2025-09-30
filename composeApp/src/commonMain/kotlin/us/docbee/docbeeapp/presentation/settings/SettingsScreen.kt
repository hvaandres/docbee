package us.docbee.docbeeapp.presentation.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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
import docbee.composeapp.generated.resources.settings_logout_title
import docbee.composeapp.generated.resources.settings_update_profile_description
import docbee.composeapp.generated.resources.settings_update_profile_title
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import us.docbee.docbeeapp.presentation.navigation.AuthenticationRoute
import us.docbee.docbeeapp.presentation.settings.effects.SettingsEffect
import us.docbee.docbeeapp.presentation.settings.events.SettingsEvent
import us.docbee.docbeeapp.presentation.theme.Black
import us.docbee.docbeeapp.presentation.theme.White

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
        SettingItem(
            title = stringResource(Res.string.settings_update_profile_title),
            description = stringResource(Res.string.settings_update_profile_description),
            icon = Res.drawable.ic_profile,
            onClick = { }
        )
        SettingItem(
            title = stringResource(Res.string.settings_contacts_title),
            description = stringResource(Res.string.settings_contacts_description),
            icon = Res.drawable.ic_contact,
            onClick = { }
        )
        SettingItem(
            title = stringResource(Res.string.settings_emergency_notification_title),
            description = stringResource(Res.string.settings_emergency_notification_description),
            icon = Res.drawable.ic_asterisk,
            onClick = { }
        )
        SettingItem(
            title = stringResource(Res.string.settings_location_title),
            description = stringResource(Res.string.settings_location_description),
            icon = Res.drawable.ic_location,
            onClick = { }
        )
        SettingItem(
            title = stringResource(Res.string.settings_logout_title),
            icon = Res.drawable.ic_logout,
            onClick = { viewModel.onEvent(SettingsEvent.OnClickLogout) }
        )
    }
}

@Composable
fun SettingItem(
    modifier: Modifier = Modifier,
    title: String,
    description: String? = null,
    icon: DrawableResource,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = White, shape = RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(24.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = Black
            )
            if (description != null) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Light,
                    color = Black
                )
            }
        }
        Image(
            modifier = Modifier.size(16.dp),
            imageVector = vectorResource(icon),
            contentDescription = null
        )
    }
}