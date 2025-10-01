package us.docbee.docbeeapp.presentation.emergency.events

sealed class EmergencyEvents {
    data object OnInit: EmergencyEvents()
    data object OnBackPressed: EmergencyEvents()
    data object OnClickImSafe: EmergencyEvents()
}