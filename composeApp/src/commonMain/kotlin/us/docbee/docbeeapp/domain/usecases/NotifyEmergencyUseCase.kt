package us.docbee.docbeeapp.domain.usecases

import us.docbee.docbeeapp.domain.models.EmergencyNotificationResult
import us.docbee.docbeeapp.domain.models.NotificationResult
import us.docbee.docbeeapp.domain.models.directory.ContactResult
import us.docbee.docbeeapp.domain.models.location.LocationResult

class NotifyEmergencyUseCase(
    private val emergency: SendEmergencyUseCase,
    private val location: FetchLocationUseCase,
    private val contacts: GetUserContactsUseCase
) {
    suspend fun sendNotification(): EmergencyNotificationResult {
        val locationResult = location.fetchCurrentLocation()
        if (locationResult !is LocationResult.Success) {
            return EmergencyNotificationResult.Error
        }

        val contactsResult = contacts.fetchUserContacts()
        if (contactsResult !is ContactResult.Success) {
            return EmergencyNotificationResult.Error
        }

        val emergencyResult = emergency.send(
            location = locationResult.location,
            phoneNumbers = contactsResult.list.map { it.phoneNumber }
        )

        val notifiedResult = if (emergencyResult is NotificationResult.Sent) {
            EmergencyNotificationResult.Sent(contactsResult.list)
        } else {
            EmergencyNotificationResult.Error
        }

        return notifiedResult
    }
}