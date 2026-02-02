package us.docbee.docbeeapp.presentation.home.states

data class UiState(
    val isPermissionNotGranted: Boolean = true,
    val showContactMissing: Boolean = true,
    val emergencyRemainingClicks: Int = 3,
    val isLoading: Boolean = true,
    val hasNoConnection: Boolean = false,
)