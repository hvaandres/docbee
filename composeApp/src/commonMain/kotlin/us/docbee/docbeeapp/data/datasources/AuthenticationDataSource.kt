package us.docbee.docbeeapp.data.datasources

import us.docbee.docbeeapp.data.entities.LogoutResponse
import us.docbee.docbeeapp.data.entities.UserAuthResponse
import us.docbee.docbeeapp.data.entities.UserCreateResponse

interface AuthenticationDataSource {
    suspend fun authenticate(email: String, password: String): UserAuthResponse

    suspend fun authenticate(idToken: String): UserAuthResponse
    suspend fun signup(email: String, password: String): UserCreateResponse
    suspend fun logout(): LogoutResponse
}

expect fun getEmailAuth(): AuthenticationDataSource