package us.docbee.docbeeapp.presentation.alerts.effects

sealed class AlertsEffects {
    data class NavigateEditAlert(val uid: String): AlertsEffects()
    data class NavigateSendAlert(val uid: String): AlertsEffects()
}