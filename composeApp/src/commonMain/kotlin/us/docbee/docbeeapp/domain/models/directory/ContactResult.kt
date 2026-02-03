package us.docbee.docbeeapp.domain.models.directory

sealed class ContactResult {
    data class Success(val list: List<ContactModel>): ContactResult()
    data object Empty: ContactResult()
    data object Error: ContactResult()
    data object Unauthorized: ContactResult()
}