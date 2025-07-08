package us.docbee.docbeeapp.presentation.login.effects

sealed class LoginEffect {
    data class ShowErrorMessage(val error: String): LoginEffect()
    data object HideErrorMessage: LoginEffect()
    data object NavigateToDashboard: LoginEffect()
}