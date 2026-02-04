package us.docbee.docbeeapp.data.strategy

import us.docbee.docbeeapp.data.EmailAuth
import us.docbee.docbeeapp.domain.mappers.AuthErrorCodesMapper
import us.docbee.docbeeapp.domain.models.login.LoginParams
import us.docbee.docbeeapp.domain.models.login.LoginResult
import us.docbee.docbeeapp.domain.strategy.LoginStrategy
import us.docbee.docbeeapp.utils.isValidEmail

class EmailLoginStrategy(
    private val emailAuth: EmailAuth
): LoginStrategy {
    override suspend fun login(credentials: LoginParams): LoginResult {
        val isInvalidEmail = !isValidEmail(credentials.email)
        val isInValidPassword = credentials.password.isEmpty()

        if (isInvalidEmail && isInValidPassword) return LoginResult.InvalidEmailAndPassword
        if (isInvalidEmail) return LoginResult.InvalidEmail
        if (isInValidPassword) return LoginResult.InvalidPassword

        val response = emailAuth.authenticate(credentials.email, credentials.password)
        return if (response.uid != null) {
            LoginResult.Success(response.uid)
        } else {
            AuthErrorCodesMapper.eval(error = response.errorCode)
        }
    }
}