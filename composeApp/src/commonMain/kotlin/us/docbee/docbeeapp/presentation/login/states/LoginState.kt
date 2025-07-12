package us.docbee.docbeeapp.presentation.login.states

data class LoginState(
    var email: String = "",
    var password: String = "",
    var rememberMe: Boolean = false,
    var isEmailInvalid: Boolean = false,
    var isPasswordInvalid: Boolean = false
)