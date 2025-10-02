package us.docbee.docbeeapp.data.repositories

import us.docbee.docbeeapp.data.entities.EmergencyNotificationRequest
import us.docbee.docbeeapp.data.services.EmergencyNotificationApiService
import us.docbee.docbeeapp.domain.models.NotificationResult
import us.docbee.docbeeapp.domain.repositories.EmergenciesRepository

class EmergencyDataRepository(
    private val service: EmergencyNotificationApiService
): EmergenciesRepository {
    override suspend fun sendNotification(notification: EmergencyNotificationRequest): NotificationResult {
        val response = service.notifyEmergency(notification)
        return if (response.isSuccess) {
            NotificationResult.Sent
        } else {
            NotificationResult.Error
        }
    }
}