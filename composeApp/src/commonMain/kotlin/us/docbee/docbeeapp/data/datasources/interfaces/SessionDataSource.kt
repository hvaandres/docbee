package us.docbee.docbeeapp.data.datasources.interfaces

import kotlinx.coroutines.flow.Flow

interface UserSessionDataSource {
    suspend fun saveSessionFlag(value: Boolean)
    suspend fun getSessionFlag(): Flow<Boolean>
    suspend fun clear()
}