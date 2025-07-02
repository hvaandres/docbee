package us.docbee.docbeeapp.data.entities

sealed class UserCreateResponse {
    data class Success(val uid: String): UserCreateResponse()
    data object AlreadyUsed: UserCreateResponse()
    data object WeakPassword: UserCreateResponse()
    data object Error: UserCreateResponse()
}