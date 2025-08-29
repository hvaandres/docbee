package us.docbee.docbeeapp.domain.repositories

import us.docbee.docbeeapp.domain.models.directory.ContactModel
import us.docbee.docbeeapp.domain.models.directory.ContactResult

interface ContactsRepository {
    suspend fun saveUserContact(uid: String, contact: ContactModel)
    suspend fun fetchUserContact(uid: String): ContactResult
}