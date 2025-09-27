package us.docbee.docbeeapp.data.datasources

import us.docbee.docbeeapp.data.entities.ContactDeleteResponse
import us.docbee.docbeeapp.data.entities.ContactFetchResponse
import us.docbee.docbeeapp.domain.models.directory.ContactModel

interface ContactRemoteDataSource {
    suspend fun saveContact(uid: String, contact: ContactModel)
    suspend fun fetchContact(uid: String): ContactFetchResponse
    suspend fun deleteContact(uid: String, contactUid: String): ContactDeleteResponse
}

expect fun getContactDataSource(): ContactRemoteDataSource