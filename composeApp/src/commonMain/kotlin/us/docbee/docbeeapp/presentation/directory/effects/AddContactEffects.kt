package us.docbee.docbeeapp.presentation.directory.effects

sealed class AddContactEffects {
    data object NavigateBack: AddContactEffects()
    data object NavigateToDirectory: AddContactEffects()
    data object OpenCountrySelector: AddContactEffects()
    data object CloseCountrySelector: AddContactEffects()
}