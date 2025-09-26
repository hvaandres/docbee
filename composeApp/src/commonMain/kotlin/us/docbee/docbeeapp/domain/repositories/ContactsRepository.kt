package us.docbee.docbeeapp.domain.repositories

import us.docbee.docbeeapp.domain.models.directory.ContactModel
import us.docbee.docbeeapp.domain.models.directory.ContactResult
import us.docbee.docbeeapp.domain.models.directory.DeleteContactResult

interface ContactsRepository {
    suspend fun saveUserContact(uid: String, contact: ContactModel)
    suspend fun fetchUserContact(uid: String): ContactResult
    suspend fun deleteUserContact(uid: String, contactUid: String): DeleteContactResult
}