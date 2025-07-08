package us.docbee.docbeeapp.data.repositories

import us.docbee.docbeeapp.data.EmailAuth
import us.docbee.docbeeapp.domain.mappers.AuthErrorCodesMapper
import us.docbee.docbeeapp.domain.models.UserAuthResult
import us.docbee.docbeeapp.domain.repositories.EmailAuthRepository

class EmailAuthDataRepository(
    private val emailAuth: EmailAuth
) : EmailAuthRepository {

    override suspend fun login(email: String, password: String): UserAuthResult {
        val response = emailAuth.authenticate(email, password)

        return if (response.uid != null) {
            UserAuthResult.Success(response.uid)
        } else {
            AuthErrorCodesMapper.eval(error = response.errorCode)
        }
    }
}