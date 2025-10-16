package us.docbee.docbeeapp.presentation.alerts

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import us.docbee.docbeeapp.domain.models.alerts.AddAlertResult
import us.docbee.docbeeapp.domain.models.alerts.AlertParams
import us.docbee.docbeeapp.domain.models.alerts.FetchAlertsResult
import us.docbee.docbeeapp.domain.usecases.alerts.GetAlertsUseCase
import us.docbee.docbeeapp.domain.usecases.alerts.SaveAlertsUseCase
import us.docbee.docbeeapp.presentation.alerts.effects.AlertsEffects
import us.docbee.docbeeapp.presentation.alerts.events.AlertsEvents
import us.docbee.docbeeapp.presentation.alerts.states.UiState
import us.docbee.docbeeapp.presentation.core.BaseViewModel

class AlertsViewModel(
    private val getAlertsUseCase: GetAlertsUseCase,
    private val saveAlertsUseCase: SaveAlertsUseCase
) : BaseViewModel<UiState, AlertsEvents, AlertsEffects>(UiState()) {

    init {
        onEvent(AlertsEvents.OnInit)
    }

    override fun onEvent(event: AlertsEvents) {
        when (event) {
            is AlertsEvents.OnInit -> onInitAlerts()
            is AlertsEvents.OnClickAddAlert -> onCLickAddAlert()
            is AlertsEvents.OnClickCloseAddAlert -> onClickCloseAddAlert()
            is AlertsEvents.OnClickAlert -> onAlertClicked()
            is AlertsEvents.OnSaveAlert -> onSaveAlertClicked(event.name, event.message)
        }
    }

    private fun onInitAlerts() {
        viewModelScope.launch {
            when (val alertsResult = getAlertsUseCase.fetchAlerts()) {
                is FetchAlertsResult.Success -> updateState { copy(alerts = alertsResult.alerts) }
                is FetchAlertsResult.Error -> Unit // TODO: Add error state screen
            }
        }
    }

    private fun onCLickAddAlert() {
        updateState { copy(isAddingAlert = true) }
    }

    private fun onClickCloseAddAlert() {
        updateState { copy(isAddingAlert = false) }
    }

    private fun onAlertClicked() {
        // TODO: Implement Functionality
    }

    private fun onSaveAlertClicked(name: String, message: String) {
        viewModelScope.launch {
            val params = AlertParams(name = name, message = message, icon = "DEFAULT")
            when(val alertsResult = saveAlertsUseCase.saveAlert(params)) {
                is AddAlertResult.Success -> updateState { copy(isAddingAlert = false) }
                is AddAlertResult.Error, is AddAlertResult.Unauthorized -> Unit
            }
        }
    }
}