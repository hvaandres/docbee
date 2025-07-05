package us.docbee.docbeeapp.presentation.login.states

data class LoginState(
    var email: String = "",
    var isButtonEnabled: Boolean = false,
    var isEmailInvalid: Boolean = false
)