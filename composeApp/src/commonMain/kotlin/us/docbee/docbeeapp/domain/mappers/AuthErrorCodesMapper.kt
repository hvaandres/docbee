package us.docbee.docbeeapp.domain.mappers

import us.docbee.docbeeapp.domain.models.login.LoginResult
import us.docbee.docbeeapp.domain.models.UserSignupResult
import us.docbee.docbeeapp.presentation.login.providers.GoogleAuthResult

object AuthErrorCodesMapper {
    fun eval(error: String?): LoginResult {
        val errorMap = mapOf(
            "ERROR_INVALID_CREDENTIAL" to LoginResult.InvalidCredentials,
            "ERROR_TOO_MANY_REQUESTS" to LoginResult.Error
        )
        return errorMap.getOrElse(error.orEmpty()) { LoginResult.Error }
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

object GoogleAuthErrorCodesMapper {
    fun eval(error: String?): GoogleAuthResult {
        val errorMap = mapOf(
            "[16] Cancelled by user." to GoogleAuthResult.Cancelled,
            "The user canceled the sign-in flow." to GoogleAuthResult.Cancelled
        )
        return errorMap.getOrElse(error.orEmpty()) { GoogleAuthResult.Error }
    }
}