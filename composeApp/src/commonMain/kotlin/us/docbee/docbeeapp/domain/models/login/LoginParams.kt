package us.docbee.docbeeapp.domain.models.login

data class LoginParams(
    val type: LoginType,
    val email: String = "",
    val password: String = "",
    val rememberMeCheck: Boolean = false
)