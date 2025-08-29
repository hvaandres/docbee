package us.docbee.docbeeapp.domain.repositories

import us.docbee.docbeeapp.domain.models.directory.UserUidResult
import us.docbee.docbeeapp.domain.models.user.UserProfile

interface UserRepository {
    suspend fun saveUserProfile(profile: UserProfile)
    suspend fun fetchUserId(): UserUidResult
}