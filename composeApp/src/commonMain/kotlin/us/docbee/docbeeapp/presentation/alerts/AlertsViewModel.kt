package us.docbee.docbeeapp.presentation.alerts

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import us.docbee.docbeeapp.domain.models.alerts.FetchAlertsResult
import us.docbee.docbeeapp.domain.usecases.GetAlertsUseCase
import us.docbee.docbeeapp.presentation.alerts.effects.AlertsEffects
import us.docbee.docbeeapp.presentation.alerts.events.AlertsEvents
import us.docbee.docbeeapp.presentation.alerts.states.UiState
import us.docbee.docbeeapp.presentation.core.BaseViewModel

class AlertsViewModel(
    private val getAlertsUseCase: GetAlertsUseCase
): BaseViewModel<UiState, AlertsEvents, AlertsEffects>(UiState()) {

    init {
        onEvent(AlertsEvents.OnInit)
    }

    override fun onEvent(event: AlertsEvents) {
        when (event) {
            is AlertsEvents.OnInit -> onInitAlerts()
            is AlertsEvents.OnClickAddAlert -> emitEffect(AlertsEffects.NavigateToAddAlert)
            is AlertsEvents.OnClickAlert -> onAlertClicked()
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

    private fun onAlertClicked() {
        // TODO: Implement Functionality
    }
}