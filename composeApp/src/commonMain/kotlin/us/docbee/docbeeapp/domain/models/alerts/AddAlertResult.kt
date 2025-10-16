package us.docbee.docbeeapp.domain.models.alerts

sealed class AddAlertResult {
    data object Success: AddAlertResult()
    data object Error: AddAlertResult()
    data object Unauthorized: AddAlertResult()
}