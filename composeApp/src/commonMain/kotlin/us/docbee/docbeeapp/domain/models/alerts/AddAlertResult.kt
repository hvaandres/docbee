package us.docbee.docbeeapp.domain.models.alerts

sealed class AddAlertResult {
    data class Success(val alert: AlertModel): AddAlertResult()
    data object Error: AddAlertResult()
    data object Unauthorized: AddAlertResult()
}