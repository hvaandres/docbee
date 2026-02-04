package us.docbee.docbeeapp.domain.repositories

import us.docbee.docbeeapp.domain.models.login.LoginResult
import us.docbee.docbeeapp.domain.models.UserSignupResult
import us.docbee.docbeeapp.domain.models.login.LoginParams

interface AuthenticationRepository {
    suspend fun login(loginParams: LoginParams): LoginResult
    suspend fun signup(email: String, password: String): UserSignupResult
}