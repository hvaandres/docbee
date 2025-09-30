package us.docbee.docbeeapp.domain.repositories

import us.docbee.docbeeapp.domain.models.session.LogoutSessionResult

interface SessionRepository {
    suspend fun saveSessionFlag(value: Boolean)
    suspend fun getSessionFlag(): Boolean
    suspend fun clearSession(): LogoutSessionResult
}