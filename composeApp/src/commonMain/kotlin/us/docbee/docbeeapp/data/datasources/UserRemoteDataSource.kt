package us.docbee.docbeeapp.data.datasources

import us.docbee.docbeeapp.domain.models.user.UserProfile

interface UserRemoteDataSource {
    suspend fun saveUser(profile: UserProfile)
}

expect fun getUserDataSource(): UserRemoteDataSource