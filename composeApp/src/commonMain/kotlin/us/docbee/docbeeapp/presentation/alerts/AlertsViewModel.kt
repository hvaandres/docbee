package us.docbee.docbeeapp.presentation.alerts

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import us.docbee.docbeeapp.domain.models.alerts.AddAlertResult
import us.docbee.docbeeapp.domain.models.alerts.AlertModifyParams
import us.docbee.docbeeapp.domain.models.alerts.AlertParams
import us.docbee.docbeeapp.domain.models.alerts.DeleteAlertResult
import us.docbee.docbeeapp.domain.models.alerts.FetchAlertsResult
import us.docbee.docbeeapp.domain.usecases.alerts.DeleteAlertsUseCase
import us.docbee.docbeeapp.domain.usecases.alerts.GetAlertsUseCase
import us.docbee.docbeeapp.domain.usecases.alerts.ModifyAlertsUseCase
import us.docbee.docbeeapp.domain.usecases.alerts.SaveAlertsUseCase
import us.docbee.docbeeapp.presentation.alerts.effects.AlertsEffects
import us.docbee.docbeeapp.presentation.alerts.events.AlertsEvents
import us.docbee.docbeeapp.presentation.alerts.states.AlertState
import us.docbee.docbeeapp.presentation.alerts.states.UiState
import us.docbee.docbeeapp.presentation.components.HorizontalSwipeState
import us.docbee.docbeeapp.presentation.core.BaseViewModel

class AlertsViewModel(
    private val getAlertsUseCase: GetAlertsUseCase,
    private val saveAlertsUseCase: SaveAlertsUseCase,
    private val deleteAlertsUseCase: DeleteAlertsUseCase,
    private val modifyAlertsUseCase: ModifyAlertsUseCase
) : BaseViewModel<UiState, AlertsEvents, AlertsEffects>(UiState()) {

    init {
        onEvent(AlertsEvents.OnInit)
    }

    override fun onEvent(event: AlertsEvents) {
        when (event) {
            is AlertsEvents.OnInit -> onInitAlerts()
            is AlertsEvents.OnClickAddAlert -> onCLickAddAlert()
            is AlertsEvents.OnClickCloseAddAlert -> onClickCloseAddAlert()
            is AlertsEvents.OnClickAlert -> onAlertClicked(event.uid)
            is AlertsEvents.OnSaveAlert -> onSaveAlertClicked(event.name, event.message)
            is AlertsEvents.OnDeleteAlert -> onDeleteAlertClicked(event.uid)
            is AlertsEvents.OnEditAlert -> onEditAlertClicked(event.uid)
            is AlertsEvents.OnSwipeAlertEvent -> onSwipeAlertEvent(event.uid, event.swipeState)
            is AlertsEvents.OnSaveEditedAlert -> onSaveEditedAlert(event.uid, event.name, event.message)
        }
    }

    private fun onInitAlerts() {
        viewModelScope.launch {
            when (val alertsResult = getAlertsUseCase.fetchAlerts()) {
                is FetchAlertsResult.Success -> {
                    updateState {
                        copy(
                            alerts = alertsResult.alerts.mapIndexed { index, alert ->
                                AlertState(
                                    uid = alert.uid,
                                    alert = alert,
                                    isSwipeable = index > 3
                                )
                            }
                        )
                    }
                }

                is FetchAlertsResult.Error -> Unit // TODO: Add error state screen
            }
        }
    }

    private fun onCLickAddAlert() {
        updateState { copy(isAddingAlert = true) }
    }

    private fun onClickCloseAddAlert() {
        updateState { copy(isAddingAlert = false, isEditingAlert = false, editingAlert = null) }
    }

    private fun onAlertClicked(uid: String) {
        emitEffect(AlertsEffects.NavigateSendAlert(uid))
        updateState {
            copy(
                alerts = alerts.map { item ->
                    if (item.uid == uid) {
                        item.copy(swipeState = HorizontalSwipeState.Closed)
                    } else {
                        item
                    }
                }
            )
        }
    }

    private fun onSaveAlertClicked(name: String, message: String) {
        viewModelScope.launch {
            val params =
                AlertParams(name = name, message = message, icon = "DEFAULT") // TODO: Add icon
            when (val alertsResult = saveAlertsUseCase.saveAlert(params)) {
                is AddAlertResult.Success -> {
                    updateState {
                        copy(
                            isAddingAlert = false,
                            alerts = alerts + AlertState(
                                uid = alertsResult.alert.uid,
                                alert = alertsResult.alert,
                                isSwipeable = true
                            )
                        )
                    }
                }

                is AddAlertResult.Error, is AddAlertResult.Unauthorized -> Unit
            }
        }
    }

    private fun onDeleteAlertClicked(uid: String) {
        viewModelScope.launch {
            when (deleteAlertsUseCase.deleteAlert(uid)) {
                is DeleteAlertResult.Success -> updateState { copy(alerts = alerts.filter { it.uid != uid }) }
                is DeleteAlertResult.Error, is DeleteAlertResult.Unauthorized -> Unit
            }
        }
    }

    private fun onEditAlertClicked(uid: String) {
        updateState {
            copy(
                isEditingAlert = true,
                editingAlert = alerts.find { it.uid == uid }?.alert,
                alerts = alerts.map { item ->
                    if (item.uid == uid) {
                        item.copy(swipeState = HorizontalSwipeState.Closed)
                    } else {
                        item
                    }
                }
            )
        }
    }

    private fun onSwipeAlertEvent(uid: String, state: HorizontalSwipeState) {
        updateState {
            copy(
                alerts = alerts.map { item ->
                    when {
                        item.uid == uid -> item.copy(swipeState = state)
                        state == HorizontalSwipeState.OpenLeft -> item.copy(swipeState = HorizontalSwipeState.Closed)
                        state == HorizontalSwipeState.OpenRight -> item.copy(swipeState = HorizontalSwipeState.Closed)
                        else -> item
                    }
                }
            )
        }
    }

    private fun onSaveEditedAlert(uid: String, name: String, message: String) {
        viewModelScope.launch {
            val params = AlertModifyParams(uid = uid, name = name, message = message, icon = "DEFAULT")
            when (val alertsResult = modifyAlertsUseCase.modifyAlert(params)) {
                is AddAlertResult.Success -> {
                    updateState {
                        copy(
                            isEditingAlert = false,
                            editingAlert = null,
                            alerts = alerts.map { item ->
                                if (item.uid == uid) {
                                    item.copy(alert = alertsResult.alert)
                                } else {
                                    item
                                }
                            }
                        )
                    }
                }

                is AddAlertResult.Error, is AddAlertResult.Unauthorized -> Unit
            }
        }
    }
}