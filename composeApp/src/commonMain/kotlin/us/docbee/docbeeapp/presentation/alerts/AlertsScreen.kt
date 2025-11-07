package us.docbee.docbeeapp.presentation.alerts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import docbee.composeapp.generated.resources.Res
import docbee.composeapp.generated.resources.alerts_add_alerts_description
import docbee.composeapp.generated.resources.alerts_add_alerts_description_alert
import docbee.composeapp.generated.resources.alerts_add_alerts_name_alert
import docbee.composeapp.generated.resources.alerts_add_alerts_save_button
import docbee.composeapp.generated.resources.alerts_add_alerts_title
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.stringResource
import us.docbee.docbeeapp.presentation.alerts.effects.AlertsEffects
import us.docbee.docbeeapp.presentation.alerts.events.AlertsEvents
import us.docbee.docbeeapp.presentation.components.CardDescriptionItem
import us.docbee.docbeeapp.presentation.components.FloatingButton
import us.docbee.docbeeapp.presentation.components.PrimaryButton
import us.docbee.docbeeapp.presentation.components.inputs.InputFieldText
import us.docbee.docbeeapp.presentation.navigation.AlertDetailRoute
import us.docbee.docbeeapp.presentation.theme.Black
import us.docbee.docbeeapp.presentation.theme.Green100
import us.docbee.docbeeapp.presentation.theme.White
import us.docbee.docbeeapp.utils.ui.SetModalStatusBar
import us.docbee.docbeeapp.utils.ui.getDrawable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertsScreen(
    parentNavController: NavHostController,
    navController: NavHostController,
    viewModel: AlertsViewModel
) {

    val uiState by viewModel.uiState.collectAsState()
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is AlertsEffects.NavigateEditAlert -> Unit // TODO: Navigate to Edit Alert
                is AlertsEffects.NavigateSendAlert -> parentNavController.navigate(AlertDetailRoute(effect.uid))
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
                        title = alert.alert.name,
                        description = alert.alert.message,
                        icon = alert.alert.icon.getDrawable(),
                        onClick = { viewModel.onEvent(AlertsEvents.OnClickAlert(alert.uid)) },
                        onDeleteClick = { viewModel.onEvent(AlertsEvents.OnDeleteAlert(alert.uid)) },
                        isSwipeable = alert.isSwipeable,
                        swipeState = alert.swipeState,
                        onSwipeChanged = {
                            viewModel.onEvent(AlertsEvents.OnSwipeAlertEvent(alert.uid, it))
                        }
                    )
                }
            }
        }
        FloatingButton(
            modifier = Modifier.align(Alignment.BottomEnd),
            onClick = { viewModel.onEvent(AlertsEvents.OnClickAddAlert) }
        )
        AddAlertBottomSheet(
            isSheetVisible = uiState.isAddingAlert,
            bottomSheetState = bottomSheetState,
            onDismissRequest = { viewModel.onEvent(AlertsEvents.OnClickCloseAddAlert) },
            onClickSave = { name, message ->
                viewModel.onEvent(AlertsEvents.OnSaveAlert(name, message))
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAlertBottomSheet(
    isSheetVisible: Boolean,
    bottomSheetState: SheetState,
    onDismissRequest: () -> Unit,
    onClickSave: (String, String) -> Unit
) {
    var alertName by remember(isSheetVisible) { mutableStateOf("") }
    var alertMessage by remember(isSheetVisible) { mutableStateOf("") }

    if (isSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            sheetState = bottomSheetState,
            containerColor = Black,
            properties = ModalBottomSheetProperties(),
            dragHandle = { BottomSheetDefaults.DragHandle(color = White, width = 150.dp) }
        ) {
            SetModalStatusBar(isSheetVisible)
            Column(
                modifier = Modifier.padding(horizontal = 56.dp).padding(bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(Res.string.alerts_add_alerts_title),
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center,
                    color = White
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(Res.string.alerts_add_alerts_description),
                    style = MaterialTheme.typography.labelLarge,
                    textAlign = TextAlign.Center,
                    color = White
                )
                Spacer(modifier = Modifier.height(16.dp))
                InputFieldText(
                    modifier = Modifier.fillMaxWidth(),
                    inputLabel = stringResource(Res.string.alerts_add_alerts_name_alert),
                    value = alertName,
                    onValueChange = { name -> alertName = name }
                )
                Spacer(modifier = Modifier.height(4.dp))
                InputFieldText(
                    modifier = Modifier.fillMaxWidth(),
                    inputLabel = stringResource(Res.string.alerts_add_alerts_description_alert),
                    value = alertMessage,
                    onValueChange = { message -> alertMessage = message }
                )
                Spacer(modifier = Modifier.height(32.dp))
                PrimaryButton(
                    modifier = Modifier.wrapContentWidth(),
                    text = stringResource(Res.string.alerts_add_alerts_save_button),
                    backgroundColor = Green100,
                    onClick = { onClickSave(alertName, alertMessage) }
                )
            }
        }
    }
}