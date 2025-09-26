package us.docbee.docbeeapp.domain.repositories

interface SessionRepository {
    suspend fun saveSessionFlag(value: Boolean)
    suspend fun getSessionFlag(): Boolean
    suspend fun clearSession()
}