package us.docbee.docbeeapp.domain.strategy

import us.docbee.docbeeapp.domain.models.login.LoginParams
import us.docbee.docbeeapp.domain.models.login.LoginResult

interface LoginStrategy {
    suspend fun login(credentials: LoginParams): LoginResult
}