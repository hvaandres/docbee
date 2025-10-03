package us.docbee.docbeeapp.domain.models

import us.docbee.docbeeapp.domain.models.directory.ContactModel

sealed class EmergencyNotificationResult {
    data class Sent(val contacts: List<ContactModel>): EmergencyNotificationResult()
    data object Error: EmergencyNotificationResult()
}