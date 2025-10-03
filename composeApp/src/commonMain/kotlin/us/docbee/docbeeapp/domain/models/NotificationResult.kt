package us.docbee.docbeeapp.domain.models

sealed class NotificationResult {
    data object Sent: NotificationResult()
    data object Error: NotificationResult()
}