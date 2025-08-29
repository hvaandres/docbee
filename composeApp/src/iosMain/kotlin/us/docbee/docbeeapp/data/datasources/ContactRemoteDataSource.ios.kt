package us.docbee.docbeeapp.data.datasources

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import us.docbee.docbeeapp.data.entities.ContactFetchResponse
import us.docbee.docbeeapp.domain.mappers.toMap
import us.docbee.docbeeapp.domain.mappers.toMutableStringMap
import us.docbee.docbeeapp.domain.models.directory.ContactModel
import us.docbee.docbeeapp.utils.FIRESTORE_COLLECTION_CONTACTS
import us.docbee.docbeeapp.utils.FIRESTORE_COLLECTION_USER
import us.docbee.docbeeapp.wrappers.ContactRemoteStorage

@OptIn(ExperimentalForeignApi::class)
class IosContactRemoteDataSource: ContactRemoteDataSource {

    private val remote = ContactRemoteStorage()

    override suspend fun saveContact(uid: String, contact: ContactModel) {
        suspendCancellableCoroutine<Unit> { thread ->
            remote.saveContactWithCollection(
                collection = FIRESTORE_COLLECTION_USER,
                collectionContact = FIRESTORE_COLLECTION_CONTACTS,
                uid = uid,
                contact = contact.toMap()
            ) { error ->
                thread.resume(Unit, null)
            }
        }
    }

    override suspend fun fetchContact(uid: String): ContactFetchResponse {
        return suspendCancellableCoroutine { thread ->
            remote.fetchContactWithCollection(
                collection = FIRESTORE_COLLECTION_USER,
                collectionContact = FIRESTORE_COLLECTION_CONTACTS,
                uid = uid
            ) { contacts, error ->
                val contactResponse = if (contacts != null) {
                    ContactFetchResponse(data = contacts.map { it.toMutableStringMap() })
                } else {
                    val errorCode = error?.userInfo?.get("FIRAuthErrorUserInfoNameKey") as? String
                    val errorMessage = error?.userInfo?.get("NSLocalizedDescription") as? String
                    ContactFetchResponse(errorCode = errorCode, errorMessage = errorMessage)
                }
                thread.resume(contactResponse, null)
            }
        }
    }
}

actual fun getContactDataSource(): ContactRemoteDataSource = IosContactRemoteDataSource()