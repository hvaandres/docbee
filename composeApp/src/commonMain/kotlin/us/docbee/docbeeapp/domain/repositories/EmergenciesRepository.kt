package us.docbee.docbeeapp.domain.repositories

import us.docbee.docbeeapp.data.entities.EmergencyNotificationRequest
import us.docbee.docbeeapp.domain.models.NotificationResult

interface EmergenciesRepository {
    suspend fun sendNotification(notification: EmergencyNotificationRequest): NotificationResult
}