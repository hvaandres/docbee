package us.docbee.docbeeapp.presentation.login.effects

sealed class AuthEffects {
    data object NavigateToDashboard: AuthEffects()
    data class ShowErrorMessage(val error: String): AuthEffects()
}