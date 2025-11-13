package us.docbee.docbeeapp.presentation.alerts.states

import us.docbee.docbeeapp.domain.models.alerts.AlertModel
import us.docbee.docbeeapp.presentation.components.HorizontalSwipeState

data class UiState(
    val isLoading: Boolean = false,
    val isAddingAlert: Boolean = false,
    val isEditingAlert: Boolean = false,
    val editingAlert: AlertModel? = null,
    val alerts: List<AlertState> = emptyList()
)

data class AlertState(
    val uid: String,
    val alert: AlertModel,
    val isSwipeable: Boolean = false,
    val swipeState: HorizontalSwipeState = HorizontalSwipeState.Closed
)