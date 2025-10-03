package us.docbee.docbeeapp.presentation.home.states

data class UiState(
    val shouldShowPermissionRequestModal: Boolean = false,
    val showContactMissing: Boolean = false
)