package us.docbee.docbeeapp.data

import us.docbee.docbeeapp.data.entities.UserAuthResponse
import us.docbee.docbeeapp.data.entities.UserCreateResponse

interface EmailAuth {
    suspend fun authenticate(email: String, password: String): UserAuthResponse
    suspend fun signup(email: String, password: String): UserCreateResponse
}

expect fun getEmailAuth(): EmailAuth