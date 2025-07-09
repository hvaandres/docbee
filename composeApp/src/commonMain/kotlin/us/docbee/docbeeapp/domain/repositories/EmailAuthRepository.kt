package us.docbee.docbeeapp.domain.repositories

import us.docbee.docbeeapp.domain.models.UserAuthResult
import us.docbee.docbeeapp.domain.models.UserSignupResult

interface EmailAuthRepository {
    suspend fun login(email: String, password: String): UserAuthResult
    suspend fun signup(email: String, password: String): UserSignupResult
}