package us.docbee.docbeeapp.domain.usecases

import us.docbee.docbeeapp.domain.models.UserSignupResult
import us.docbee.docbeeapp.domain.repositories.EmailAuthRepository

class EmailSignupUseCase(
    private val emailAuthRepository: EmailAuthRepository
) {
    suspend fun createUser(email: String, password: String): UserSignupResult {
        return emailAuthRepository.signup(email, password)
    }
}