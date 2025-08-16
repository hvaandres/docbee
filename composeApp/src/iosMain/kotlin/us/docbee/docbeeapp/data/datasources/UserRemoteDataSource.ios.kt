package us.docbee.docbeeapp.data.datasources

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import us.docbee.docbeeapp.domain.mappers.toMap
import us.docbee.docbeeapp.domain.models.user.UserProfile
import us.docbee.docbeeapp.utils.FIRESTORE_COLLECTION_USER
import us.docbee.docbeeapp.wrappers.UserRemoteStorage

@OptIn(ExperimentalForeignApi::class, ExperimentalCoroutinesApi::class)
class IosUserRemoteDataSource : UserRemoteDataSource {

    private val remote = UserRemoteStorage()

    override suspend fun saveUser(profile: UserProfile) {
        suspendCancellableCoroutine<Unit> { thread ->
            remote.saveUserWithCollection(
                collection = FIRESTORE_COLLECTION_USER,
                profile = profile.toMap()
            ) { error ->
                thread.resume(Unit, null)
            }
        }
    }
}

actual fun getUserDataSource(): UserRemoteDataSource = IosUserRemoteDataSource()