package us.docbee.docbeeapp.presentation.settings.effects

sealed class SettingsEffect {
    data object NavigateToLogin: SettingsEffect()
}