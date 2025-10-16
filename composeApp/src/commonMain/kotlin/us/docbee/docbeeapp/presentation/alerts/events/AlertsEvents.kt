package us.docbee.docbeeapp.presentation.alerts.events

sealed class AlertsEvents {
    data object OnInit: AlertsEvents()
    data object OnClickAddAlert: AlertsEvents()
    data object OnClickCloseAddAlert: AlertsEvents()
    data class OnClickAlert(val uid: String): AlertsEvents()
    data class OnSaveAlert(val name: String, val message: String): AlertsEvents()
}