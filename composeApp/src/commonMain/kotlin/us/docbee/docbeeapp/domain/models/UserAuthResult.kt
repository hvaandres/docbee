package us.docbee.docbeeapp.domain.models

sealed class UserAuthResult {
    data class Success(val uid: String): UserAuthResult()
    data object InvalidCredentials: UserAuthResult()
    data object Error: UserAuthResult()
}