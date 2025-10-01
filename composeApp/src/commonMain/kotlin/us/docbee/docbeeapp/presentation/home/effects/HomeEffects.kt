package us.docbee.docbeeapp.presentation.home.effects

sealed class HomeEffects {
    data object NavigateToEmergency: HomeEffects()
    data object NavigateToSettings: HomeEffects()
}