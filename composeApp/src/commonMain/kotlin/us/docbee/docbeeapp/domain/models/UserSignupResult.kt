package us.docbee.docbeeapp.domain.models

sealed class UserSignupResult {
    data class Success(val uid: String): UserSignupResult()
    data object AlreadyUsed: UserSignupResult()
    data object WeakPassword: UserSignupResult()
    data object Error: UserSignupResult()
}