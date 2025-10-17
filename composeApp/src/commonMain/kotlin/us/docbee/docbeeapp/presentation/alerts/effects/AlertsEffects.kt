package us.docbee.docbeeapp.presentation.alerts.effects

sealed class AlertsEffects {
    data class NavigateEditAlert(val uid: String): AlertsEffects()
}