package us.docbee.docbeeapp.domain.usecases.login

import us.docbee.docbeeapp.domain.models.login.LoginParams
import us.docbee.docbeeapp.domain.models.login.LoginResult
import us.docbee.docbeeapp.domain.repositories.AuthenticationRepository
import us.docbee.docbeeapp.domain.repositories.SessionRepository

class LoginUseCase(
    private val loginRepository: AuthenticationRepository,
    private val sessionRepository: SessionRepository
) {
    suspend fun login(loginParams: LoginParams): LoginResult {
        val response = loginRepository.login(loginParams)

        if (response is LoginResult.Success) {
            sessionRepository.saveSessionFlag(loginParams.rememberMeCheck)
        }

        return response
    }
}