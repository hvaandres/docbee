package us.docbee.docbeeapp.data.entities

import kotlinx.serialization.Serializable

@Serializable
data class EmergencyNotificationResponse(
    val isSuccess: Boolean = false
)