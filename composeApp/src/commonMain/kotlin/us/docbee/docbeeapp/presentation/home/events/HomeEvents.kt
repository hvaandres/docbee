package us.docbee.docbeeapp.presentation.home.events

sealed class HomeEvents {
    data object OnValidateRequirements: HomeEvents()
    data object OnClickEmergency: HomeEvents()
    data object OnOpenSettings: HomeEvents()
    data object OnClickContacts: HomeEvents()
}
