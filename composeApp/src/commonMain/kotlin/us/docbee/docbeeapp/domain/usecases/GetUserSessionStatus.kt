package us.docbee.docbeeapp.domain.usecases

import us.docbee.docbeeapp.domain.models.directory.UserUidResult
import us.docbee.docbeeapp.domain.models.splash.SessionResult
import us.docbee.docbeeapp.domain.repositories.SessionRepository
import us.docbee.docbeeapp.domain.repositories.UserRepository

class GetUserSessionStatus(
    private val sessionRepository: SessionRepository,
    private val userRepository: UserRepository
) {

    suspend fun checkSessionStatus(): SessionResult {
        val isRememberedSession = sessionRepository.getSessionFlag()
        val userSessionStatus = userRepository.fetchUserId()

        return if (isRememberedSession && userSessionStatus is UserUidResult.Success) {
            SessionResult.UserLogged
        } else {
            SessionResult.UserNoLogged
        }
    }
}