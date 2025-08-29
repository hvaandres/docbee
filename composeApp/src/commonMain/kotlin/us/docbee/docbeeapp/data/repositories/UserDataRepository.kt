package us.docbee.docbeeapp.data.repositories

import us.docbee.docbeeapp.data.datasources.UserRemoteDataSource
import us.docbee.docbeeapp.domain.models.directory.UserUidResult
import us.docbee.docbeeapp.domain.models.user.UserProfile
import us.docbee.docbeeapp.domain.repositories.UserRepository

class UserDataRepository(
    private val userDataSource: UserRemoteDataSource
): UserRepository {
    override suspend fun saveUserProfile(profile: UserProfile) {
        userDataSource.saveUser(profile)
    }

    override suspend fun fetchUserId(): UserUidResult {
        val response = userDataSource.fetchUserUid()

        return if (response.uid != null) {
            UserUidResult.Success(response.uid)
        } else {
            UserUidResult.Unauthorized
        }
    }
}