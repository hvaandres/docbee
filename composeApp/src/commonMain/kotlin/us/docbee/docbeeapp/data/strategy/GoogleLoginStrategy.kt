package us.docbee.docbeeapp.data.strategy

import us.docbee.docbeeapp.data.datasources.AuthenticationDataSource
import us.docbee.docbeeapp.domain.mappers.AuthErrorCodesMapper
import us.docbee.docbeeapp.domain.models.login.LoginParams
import us.docbee.docbeeapp.domain.models.login.LoginResult
import us.docbee.docbeeapp.domain.strategy.LoginStrategy
import us.docbee.docbeeapp.presentation.login.providers.GoogleAuthProvider

class GoogleLoginStrategy(
    private val googleAuthProvider: GoogleAuthProvider,
    private val authenticationDataSource: AuthenticationDataSource
): LoginStrategy {
    override suspend fun login(credentials: LoginParams): LoginResult {
        val idToken = googleAuthProvider.getGoogleIdToken()
        if (idToken.isEmpty()) return LoginResult.CancelOperation
        val response = authenticationDataSource.authenticate(idToken)
        return if (response.uid != null) {
            LoginResult.Success(response.uid)
        } else {
            AuthErrorCodesMapper.eval(error = response.errorCode)
        }
    }
}