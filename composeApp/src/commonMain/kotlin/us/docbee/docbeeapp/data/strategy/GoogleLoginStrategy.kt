package us.docbee.docbeeapp.data.strategy

import us.docbee.docbeeapp.domain.models.login.LoginParams
import us.docbee.docbeeapp.domain.models.login.LoginResult
import us.docbee.docbeeapp.domain.strategy.LoginStrategy

class GoogleLoginStrategy: LoginStrategy {
    override suspend fun login(credentials: LoginParams): LoginResult {
        return LoginResult.Error
    }
}