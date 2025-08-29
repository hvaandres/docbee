package us.docbee.docbeeapp.domain.models.directory

sealed class UserUidResult {
    data class Success(val uid: String): UserUidResult()
    data object Unauthorized: UserUidResult()
}
