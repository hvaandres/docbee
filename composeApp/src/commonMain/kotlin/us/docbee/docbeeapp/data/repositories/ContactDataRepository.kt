package us.docbee.docbeeapp.data.repositories

import us.docbee.docbeeapp.data.datasources.ContactRemoteDataSource
import us.docbee.docbeeapp.domain.mappers.toContactDomain
import us.docbee.docbeeapp.domain.models.directory.ContactModel
import us.docbee.docbeeapp.domain.models.directory.ContactResult
import us.docbee.docbeeapp.domain.repositories.ContactsRepository

class ContactDataRepository(val contactDataSource: ContactRemoteDataSource): ContactsRepository {

    override suspend fun saveUserContact(uid: String, contact: ContactModel) {
        contactDataSource.saveContact(uid, contact)
    }

    override suspend fun fetchUserContact(uid: String): ContactResult {
        val response = contactDataSource.fetchContact(uid)
        return when {
            response.errorCode != null -> ContactResult.Error
            response.data.isNullOrEmpty() -> ContactResult.Empty
            else -> ContactResult.Success(list = response.data.mapNotNull { it?.toContactDomain() })
        }
    }
}