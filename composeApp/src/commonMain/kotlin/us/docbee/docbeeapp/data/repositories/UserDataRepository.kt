package us.docbee.docbeeapp.data.repositories

import us.docbee.docbeeapp.data.datasources.UserRemoteDataSource
import us.docbee.docbeeapp.domain.models.user.UserProfile
import us.docbee.docbeeapp.domain.repositories.UserRepository

class UserDataRepository(
    private val userDataSource: UserRemoteDataSource
): UserRepository {
    override suspend fun saveUserProfile(profile: UserProfile) {
        userDataSource.saveUser(profile)
    }
}