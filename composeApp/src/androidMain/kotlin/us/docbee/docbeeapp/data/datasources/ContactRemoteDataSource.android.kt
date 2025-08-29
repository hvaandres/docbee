package us.docbee.docbeeapp.data.datasources

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
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

            ContactFetchResponse(data = query.documents.map { it.data })

        } catch (ex: Exception) {
            ContactFetchResponse(errorCode = "UNKNOWN ERROR", errorMessage = ex.localizedMessage)
        }
    }
}

actual fun getContactDataSource(): ContactRemoteDataSource = AndroidContactRemoteDataSource()