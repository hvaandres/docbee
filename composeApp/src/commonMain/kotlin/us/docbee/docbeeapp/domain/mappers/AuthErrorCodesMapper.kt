package us.docbee.docbeeapp.domain.mappers

import us.docbee.docbeeapp.domain.models.UserAuthResult
import us.docbee.docbeeapp.domain.models.UserSignupResult

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
    fun eval(error: String?): UserSignupResult {
        val errorMap = mapOf(
            "ERROR_EMAIL_ALREADY_IN_USE" to UserSignupResult.AlreadyUsed,
            "ERROR_WEAK_PASSWORD" to UserSignupResult.WeakPassword,
            "ERROR_INTERNAL_ERROR" to UserSignupResult.WeakPassword
        )
        return errorMap.getOrElse(error.orEmpty()) { UserSignupResult.Error }
    }
}