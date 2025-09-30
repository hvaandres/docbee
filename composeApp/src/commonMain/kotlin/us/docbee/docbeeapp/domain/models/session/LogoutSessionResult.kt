package us.docbee.docbeeapp.domain.models.session

sealed class LogoutSessionResult {
    data object Success: LogoutSessionResult()
    data object Error: LogoutSessionResult()
}