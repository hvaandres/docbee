package us.docbee.docbeeapp.domain.repositories

import us.docbee.docbeeapp.domain.models.UserAuthResult

interface EmailAuthRepository {
    suspend fun login(email: String, password: String): UserAuthResult
}