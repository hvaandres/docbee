package us.docbee.docbeeapp.domain.usecases

import us.docbee.docbeeapp.domain.models.UserAuthResult
import us.docbee.docbeeapp.domain.repositories.EmailAuthRepository

class EmailAuthUseCase(
    private val emailAuthRepository: EmailAuthRepository
) {
    suspend fun authenticate(email: String, password: String): UserAuthResult {
        return emailAuthRepository.login(email, password)
    }
}