package us.docbee.docbeeapp.presentation.alerts.states

import us.docbee.docbeeapp.domain.models.alerts.AlertModel

data class UiState(
    val isLoading: Boolean = false,
    val alerts: List<AlertModel> = emptyList()
)