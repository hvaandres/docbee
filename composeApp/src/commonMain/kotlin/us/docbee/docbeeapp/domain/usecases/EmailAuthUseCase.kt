package us.docbee.docbeeapp.domain.usecases

import us.docbee.docbeeapp.domain.models.UserAuthResult
import us.docbee.docbeeapp.domain.repositories.EmailAuthRepository
import us.docbee.docbeeapp.utils.isValidEmail

class EmailAuthUseCase(
    private val emailAuthRepository: EmailAuthRepository
) {
    suspend fun authenticate(email: String, password: String, rememberMeCheck: Boolean): UserAuthResult {
        val isInvalidEmail = !isValidEmail(email)
        val isInValidPassword = password.isEmpty()
        return when {
            isInvalidEmail && isInValidPassword -> UserAuthResult.InvalidEmailAndPassword
            isInvalidEmail -> UserAuthResult.InvalidEmail
            isInValidPassword -> UserAuthResult.InvalidPassword
            else -> emailAuthRepository.login(email, password)
        }
    }
}