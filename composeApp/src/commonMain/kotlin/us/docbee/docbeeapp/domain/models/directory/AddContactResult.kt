package us.docbee.docbeeapp.domain.models.directory

sealed class AddContactResult {
    data object Success: AddContactResult()
    data object Error: AddContactResult()
    data object Unauthorized: AddContactResult()
}