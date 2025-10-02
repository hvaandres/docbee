package us.docbee.docbeeapp.presentation.emergency.effects

sealed class EmergencyEffects {
    data object NavigateBack: EmergencyEffects()
    data object NotifyImSafe: EmergencyEffects()
}