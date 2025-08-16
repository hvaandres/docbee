package us.docbee.docbeeapp.data.datasources

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import us.docbee.docbeeapp.domain.mappers.toMap
import us.docbee.docbeeapp.domain.models.user.UserProfile
import us.docbee.docbeeapp.utils.FIRESTORE_COLLECTION_USER

class AndroidUserRemoteDataSource : UserRemoteDataSource {
    override suspend fun saveUser(profile: UserProfile) {
        FirebaseFirestore.getInstance()
            .collection(FIRESTORE_COLLECTION_USER)
            .document(profile.uid)
            .set(profile.toMap())
            .await()
    }
}

actual fun getUserDataSource(): UserRemoteDataSource = AndroidUserRemoteDataSource()