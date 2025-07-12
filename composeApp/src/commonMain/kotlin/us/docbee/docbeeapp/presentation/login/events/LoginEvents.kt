package us.docbee.docbeeapp.presentation.login.events

sealed class LoginEvents {
    data class OnChangeEmailField(val email: String): LoginEvents()
    data class OnChangePasswordField(val password: String): LoginEvents()
    data class OnRememberCheckBox(val isChecked: Boolean): LoginEvents()
    data object OnLoginClickButton: LoginEvents()
    data object OnForgotPasswordClick: LoginEvents()
    data object OnResetEvent: LoginEvents()
}