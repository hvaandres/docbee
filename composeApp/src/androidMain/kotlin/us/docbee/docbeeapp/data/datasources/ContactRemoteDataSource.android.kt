package us.docbee.docbeeapp.data.datasources

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import us.docbee.docbeeapp.data.entities.ContactDeleteResponse
import us.docbee.docbeeapp.data.entities.ContactFetchResponse
import us.docbee.docbeeapp.domain.models.directory.ContactModel
import us.docbee.docbeeapp.utils.FIRESTORE_COLLECTION_CONTACTS
import us.docbee.docbeeapp.utils.FIRESTORE_COLLECTION_USER

class AndroidContactRemoteDataSource : ContactRemoteDataSource {
    private val db = FirebaseFirestore.getInstance()

    override suspend fun saveContact(uid: String, contact: ContactModel) {
        val reference = db
            .collection(FIRESTORE_COLLECTION_USER)
            .document(uid)
            .collection(FIRESTORE_COLLECTION_CONTACTS)
            .document()

        contact.uid = reference.id

        reference
            .set(contact)
            .await()
    }

    override suspend fun fetchContact(uid: String): ContactFetchResponse {
        return try {
            val query = db.collection(FIRESTORE_COLLECTION_USER)
                .document(uid)
                .collection(FIRESTORE_COLLECTION_CONTACTS)
                .get()
                .await()

            ContactFetchResponse(data = query.documents.map { it.toObject<ContactModel>() as ContactModel })

        } catch (ex: Exception) {
            ContactFetchResponse(errorCode = "UNKNOWN_ERROR", errorMessage = ex.localizedMessage)
        }
    }

    override suspend fun deleteContact(uid: String, contactUid: String): ContactDeleteResponse {
        return try {
            db.collection(FIRESTORE_COLLECTION_USER)
                .document(uid)
                .collection(FIRESTORE_COLLECTION_CONTACTS)
                .document(contactUid)
                .delete()

            ContactDeleteResponse(successDeleted = true)
        } catch (ex: Exception) {
            ContactDeleteResponse(errorCode = "UNKNOWN_ERROR", errorMessage = ex.localizedMessage)
        }
    }
}

actual fun getContactDataSource(): ContactRemoteDataSource = AndroidContactRemoteDataSource()