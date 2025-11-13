package us.docbee.docbeeapp.presentation.alerts.effects

sealed class AlertsEffects {
    data class NavigateSendAlert(val uid: String): AlertsEffects()
}