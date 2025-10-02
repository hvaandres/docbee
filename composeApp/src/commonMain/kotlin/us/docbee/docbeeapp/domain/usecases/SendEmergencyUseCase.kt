package us.docbee.docbeeapp.domain.usecases

import us.docbee.docbeeapp.data.entities.EmergencyNotificationRequest
import us.docbee.docbeeapp.domain.models.NotificationResult
import us.docbee.docbeeapp.domain.models.location.LocationData
import us.docbee.docbeeapp.domain.repositories.EmergenciesRepository

class SendEmergencyUseCase(private val emergency: EmergenciesRepository) {

    suspend fun send(
        location: LocationData,
        phoneNumbers: List<String>
    ): NotificationResult {
        val request = EmergencyNotificationRequest(
            contacts = phoneNumbers,
            latitude = location.latitude,
            longitude = location.longitude
        )

        return emergency.sendNotification(request)
    }
}