package us.docbee.docbeeapp.domain.mappers

import us.docbee.docbeeapp.data.entities.UserCreateResponse
import us.docbee.docbeeapp.domain.models.UserAuthResult

object AuthErrorCodesMapper {
    fun eval(error: String?): UserAuthResult {
        val errorMap = mapOf(
            "ERROR_INVALID_CREDENTIAL" to UserAuthResult.InvalidCredentials,
            "ERROR_TOO_MANY_REQUESTS" to UserAuthResult.Error
        )
        return errorMap.getOrElse(error.orEmpty()) { UserAuthResult.Error }
    }
}

object CreateErrorCodesMapper {
    fun eval(error: String?): UserCreateResponse {
        val errorMap = mapOf(
            "ERROR_EMAIL_ALREADY_IN_USE" to UserCreateResponse.AlreadyUsed,
            "ERROR_WEAK_PASSWORD" to UserCreateResponse.WeakPassword,
            "ERROR_INTERNAL_ERROR" to UserCreateResponse.WeakPassword
        )
        return errorMap.getOrElse(error.orEmpty()) { UserCreateResponse.Error }
    }
}