package us.docbee.docbeeapp.domain.usecases

import us.docbee.docbeeapp.domain.models.session.LogoutSessionResult
import us.docbee.docbeeapp.domain.repositories.SessionRepository

class LogoutUseCase(
    private val sessionRepository: SessionRepository
) {
    suspend fun doLogout(): LogoutSessionResult {
        return sessionRepository.clearSession()
    }
}