package us.docbee.docbeeapp.presentation.login.events

sealed class LoginEvents {
    data class OnChangeEmailField(val email: String): LoginEvents()
    data object OnLoginClickButton: LoginEvents()
    data object OnDismissModalError: LoginEvents()
}