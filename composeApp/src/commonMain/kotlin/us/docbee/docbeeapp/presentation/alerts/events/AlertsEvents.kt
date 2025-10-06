package us.docbee.docbeeapp.presentation.alerts.events

sealed class AlertsEvents {
    data object OnInit: AlertsEvents()
    data object OnClickAddAlert: AlertsEvents()
    data class OnClickAlert(val uid: String): AlertsEvents()
}