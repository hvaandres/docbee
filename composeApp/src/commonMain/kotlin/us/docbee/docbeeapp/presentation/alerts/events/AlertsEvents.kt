package us.docbee.docbeeapp.presentation.alerts.events

import us.docbee.docbeeapp.presentation.components.HorizontalSwipeState

sealed class AlertsEvents {
    data object OnInit : AlertsEvents()
    data object OnClickAddAlert : AlertsEvents()
    data object OnClickCloseAddAlert : AlertsEvents()
    data class OnClickAlert(val uid: String) : AlertsEvents()
    data class OnSaveAlert(val name: String, val message: String) : AlertsEvents()
    data class OnDeleteAlert(val uid: String) : AlertsEvents()
    data class OnEditAlert(val uid: String): AlertsEvents()
    data class OnSwipeAlertEvent(val uid: String, val swipeState: HorizontalSwipeState) : AlertsEvents()
}