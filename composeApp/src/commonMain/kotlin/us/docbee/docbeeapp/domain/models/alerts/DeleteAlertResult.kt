package us.docbee.docbeeapp.domain.models.alerts

sealed class DeleteAlertResult {
    data object Success: DeleteAlertResult()
    data object Error: DeleteAlertResult()
    data object Unauthorized: DeleteAlertResult()
}