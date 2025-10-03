package us.docbee.docbeeapp.data.entities

data class EmergencyNotificationRequest(
    val contacts: List<String> = emptyList(),
    val latitude: Double,
    val longitude: Double
)