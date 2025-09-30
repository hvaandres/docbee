package us.docbee.docbeeapp.data.entities

sealed class LogoutResponse {
    data object Success: LogoutResponse()
    data object Error: LogoutResponse()
}