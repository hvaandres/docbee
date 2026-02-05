package us.docbee.docbeeapp.data.strategy

import us.docbee.docbeeapp.data.datasources.AuthenticationDataSource
import us.docbee.docbeeapp.domain.mappers.AuthErrorCodesMapper
import us.docbee.docbeeapp.domain.models.login.LoginParams
import us.docbee.docbeeapp.domain.models.login.LoginResult
import us.docbee.docbeeapp.domain.strategy.LoginStrategy
import us.docbee.docbeeapp.presentation.login.providers.GoogleAuthProvider
import us.docbee.docbeeapp.presentation.login.providers.GoogleAuthResult

class GoogleLoginStrategy(
    private val googleAuthProvider: GoogleAuthProvider,
    private val authenticationDataSource: AuthenticationDataSource
): LoginStrategy {
    override suspend fun login(credentials: LoginParams): LoginResult {
        return when (val tokenResult = googleAuthProvider.getGoogleIdToken()) {
            is GoogleAuthResult.Cancelled -> LoginResult.CancelOperation
            is GoogleAuthResult.Error -> LoginResult.Error
            is GoogleAuthResult.Success -> authenticate(tokenResult.idToken)
        }
    }

    private suspend fun authenticate(token: String): LoginResult {
        val response = authenticationDataSource.authenticate(token)
        return if (response.uid != null) {
            LoginResult.Success(response.uid)
        } else {
            AuthErrorCodesMapper.eval(error = response.errorCode)
        }
    }
}