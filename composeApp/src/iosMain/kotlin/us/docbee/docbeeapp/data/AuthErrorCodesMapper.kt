package us.docbee.docbeeapp.data

import us.docbee.docbeeapp.data.entities.UserAuthResponse
import us.docbee.docbeeapp.data.entities.UserCreateResponse

object AuthErrorCodesMapper {
    fun eval(error: String?): UserAuthResponse {
        val errorMap = mapOf(
            "ERROR_INVALID_CREDENTIAL" to UserAuthResponse.InvalidCredentials,
            "ERROR_TOO_MANY_REQUESTS" to UserAuthResponse.Error
        )
        return errorMap.getOrElse(error.orEmpty()) { UserAuthResponse.Error }
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