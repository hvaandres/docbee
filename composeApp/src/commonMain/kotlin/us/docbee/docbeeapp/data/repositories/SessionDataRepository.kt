package us.docbee.docbeeapp.data.repositories

import kotlinx.coroutines.flow.first
import us.docbee.docbeeapp.data.EmailAuth
import us.docbee.docbeeapp.data.datasources.interfaces.UserSessionDataSource
import us.docbee.docbeeapp.data.entities.LogoutResponse
import us.docbee.docbeeapp.domain.models.session.LogoutSessionResult
import us.docbee.docbeeapp.domain.repositories.SessionRepository

class SessionDataRepository(
    private val sessionDataSource: UserSessionDataSource,
    private val userAuthentication: EmailAuth
) : SessionRepository {
    override suspend fun saveSessionFlag(value: Boolean) {
        sessionDataSource.saveSessionFlag(value)
    }

    override suspend fun getSessionFlag(): Boolean {
        return sessionDataSource.getSessionFlag().first()
    }

    override suspend fun clearSession(): LogoutSessionResult {
        sessionDataSource.clear()
        val response = userAuthentication.logout()
        return if (response is LogoutResponse.Success) {
            LogoutSessionResult.Success
        } else {
            LogoutSessionResult.Error
        }
    }
}