package us.docbee.docbeeapp.data.repositories

import us.docbee.docbeeapp.data.EmailAuth
import us.docbee.docbeeapp.data.strategy.LoginStrategyFactory
import us.docbee.docbeeapp.domain.mappers.CreateErrorCodesMapper
import us.docbee.docbeeapp.domain.models.UserSignupResult
import us.docbee.docbeeapp.domain.models.login.LoginParams
import us.docbee.docbeeapp.domain.models.login.LoginResult
import us.docbee.docbeeapp.domain.repositories.AuthenticationRepository

class AuthenticationDataRepository(
    private val emailAuth: EmailAuth, //this will remove when signup be split to a new class
    private val loginStrategyFactory: LoginStrategyFactory
) : AuthenticationRepository {

    override suspend fun login(loginParams: LoginParams): LoginResult {
        val strategy = loginStrategyFactory.getStrategy(loginParams.type)
        return strategy.login(loginParams)
    }

    override suspend fun signup(email: String, password: String): UserSignupResult {
        val response = emailAuth.signup(email, password)

        return if (response.uid != null) {
            UserSignupResult.Success(response.uid)
        } else {
            CreateErrorCodesMapper.eval(error = response.errorCode)
        }
    }
}