package us.docbee.docbeeapp.presentation.login.effects

sealed class SignupEffect {
    data class ShowErrorMessage(val error: String): SignupEffect()
    data object NavigateToDashboard: SignupEffect()
    data object OpenCountrySelector: SignupEffect()
    data object CloseCountrySelector: SignupEffect()
}