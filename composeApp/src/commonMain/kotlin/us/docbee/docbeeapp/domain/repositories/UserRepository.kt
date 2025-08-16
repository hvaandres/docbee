package us.docbee.docbeeapp.domain.repositories

import us.docbee.docbeeapp.domain.models.user.UserProfile

interface UserRepository {
    suspend fun saveUserProfile(profile: UserProfile)
}