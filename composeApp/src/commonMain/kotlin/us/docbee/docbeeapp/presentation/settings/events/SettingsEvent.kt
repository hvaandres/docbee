package us.docbee.docbeeapp.presentation.settings.events

sealed class SettingsEvent {
    data object OnClickUpdateProfile: SettingsEvent()
    data object OnClickContacts: SettingsEvent()
    data object OnClickEmergencyNotification: SettingsEvent()
    data object OnClickLocation: SettingsEvent()
    data object OnClickLogout: SettingsEvent()
}