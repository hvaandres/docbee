package us.docbee.docbeeapp.data.datasources

import us.docbee.docbeeapp.data.entities.UserUidResponse
import us.docbee.docbeeapp.domain.models.user.UserProfile

interface UserRemoteDataSource {
    suspend fun saveUser(profile: UserProfile)
    suspend fun fetchUserUid(): UserUidResponse
}

expect fun getUserDataSource(): UserRemoteDataSource