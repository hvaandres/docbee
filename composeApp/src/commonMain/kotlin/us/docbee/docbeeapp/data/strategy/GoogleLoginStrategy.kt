package us.docbee.docbeeapp.data.strategy

import us.docbee.docbeeapp.data.datasources.AuthenticationDataSource
import us.docbee.docbeeapp.data.datasources.UserRemoteDataSource
import us.docbee.docbeeapp.domain.mappers.AuthErrorCodesMapper
import us.docbee.docbeeapp.domain.models.login.AuthenticationModel
import us.docbee.docbeeapp.domain.models.login.LoginParams
import us.docbee.docbeeapp.domain.models.login.LoginResult
import us.docbee.docbeeapp.domain.models.user.UserProfile
import us.docbee.docbeeapp.domain.strategy.LoginStrategy
import us.docbee.docbeeapp.presentation.login.providers.GoogleAuthProvider
import us.docbee.docbeeapp.presentation.login.providers.GoogleAuthResult
import kotlin.time.Clock

class GoogleLoginStrategy(
    private val googleAuthProvider: GoogleAuthProvider,
    private val authenticationDataSource: AuthenticationDataSource,
    private val userDataSource: UserRemoteDataSource
) : LoginStrategy {
    override suspend fun login(credentials: LoginParams): LoginResult {
        return when (val googleData = googleAuthProvider.getGoogleAuthData()) {
            is GoogleAuthResult.Cancelled -> LoginResult.CancelOperation
            is GoogleAuthResult.Error -> LoginResult.Error
            is GoogleAuthResult.Success -> authenticate(googleData.user)
        }
    }

    private suspend fun authenticate(data: AuthenticationModel): LoginResult {
        val response = authenticationDataSource.authenticate(data.idToken)
        return if (response.uid != null) {
            createProfile(uid = response.uid, isNewUser = response.isNewUser, data = data)
            LoginResult.Success(response.uid)
        } else {
            AuthErrorCodesMapper.eval(error = response.errorCode)
        }
    }

    private suspend fun createProfile(uid: String, isNewUser: Boolean, data: AuthenticationModel) {
        if (!isNewUser) return
        userDataSource.saveUser(
            UserProfile(
                uid = uid,
                name = data.name,
                lastname = data.lastname,
                email = data.email,
                phoneNumber = data.phoneNumber,
                dateOfBirth = "",
                createdAt = Clock.System.now().toEpochMilliseconds()
            )
        )
    }
}