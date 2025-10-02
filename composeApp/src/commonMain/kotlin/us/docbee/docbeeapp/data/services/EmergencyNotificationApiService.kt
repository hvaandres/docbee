package us.docbee.docbeeapp.data.services

import us.docbee.docbeeapp.data.entities.EmergencyNotificationRequest
import us.docbee.docbeeapp.data.entities.EmergencyNotificationResponse

class EmergencyNotificationApiService {
    suspend fun notifyEmergency(body: EmergencyNotificationRequest): EmergencyNotificationResponse {
        return EmergencyNotificationResponse(true) // TODO: Add Service to Notify Contacts
    }
}