package us.docbee.docbeeapp.data.strategy

import us.docbee.docbeeapp.domain.models.login.LoginType
import us.docbee.docbeeapp.domain.strategy.LoginStrategy

class LoginStrategyFactory(
    private val emailStrategy: EmailLoginStrategy,
    private val googleStrategy: GoogleLoginStrategy,
    private val appleStrategy: AppleLoginStrategy
) {
    fun getStrategy(type: LoginType): LoginStrategy {
        return when (type) {
            LoginType.EMAIL -> emailStrategy
            LoginType.GOOGLE -> googleStrategy
            LoginType.APPLE -> appleStrategy
        }
    }
}