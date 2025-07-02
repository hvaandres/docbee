package us.docbee.docbeeapp.data.entities

sealed class UserAuthResponse {
    data class Success(val uid: String): UserAuthResponse()
    data object InvalidCredentials: UserAuthResponse()
    data object Error: UserAuthResponse()
}