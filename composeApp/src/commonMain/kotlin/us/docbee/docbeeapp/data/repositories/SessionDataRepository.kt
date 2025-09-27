package us.docbee.docbeeapp.data.repositories

import kotlinx.coroutines.flow.first
import us.docbee.docbeeapp.data.datasources.interfaces.UserSessionDataSource
import us.docbee.docbeeapp.domain.repositories.SessionRepository

class SessionDataRepository(
    private val sessionDataSource: UserSessionDataSource
) : SessionRepository {
    override suspend fun saveSessionFlag(value: Boolean) {
        sessionDataSource.saveSessionFlag(value)
    }

    override suspend fun getSessionFlag(): Boolean {
        return sessionDataSource.getSessionFlag().first()
    }

    override suspend fun clearSession() {
        sessionDataSource.clear()
    }
}