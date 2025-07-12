package us.docbee.docbeeapp.domain.models

sealed class UserAuthResult {
    data class Success(val uid: String): UserAuthResult()
    data object InvalidCredentials: UserAuthResult()
    data object Error: UserAuthResult()
    data object InvalidEmail: UserAuthResult()
    data object InvalidPassword: UserAuthResult()
    data object InvalidEmailAndPassword: UserAuthResult()
}