package us.docbee.docbeeapp.presentation.home

import us.docbee.docbeeapp.presentation.core.BaseViewModel
import us.docbee.docbeeapp.presentation.home.effects.HomeEffects
import us.docbee.docbeeapp.presentation.home.events.HomeEvents
import us.docbee.docbeeapp.presentation.home.states.UiState
import us.docbee.docbeeapp.utils.ui.permissions.LocationPermissionManager
import us.docbee.docbeeapp.utils.ui.permissions.PermissionResult

class HomeViewModel(
    private val permissionManager: LocationPermissionManager
) : BaseViewModel<UiState, HomeEvents, HomeEffects>(UiState()) {

    override fun onEvent(event: HomeEvents) {
        when (event) {
            HomeEvents.OnClickEmergency -> onClickEmergency()
            HomeEvents.OnOpenSettings -> onOpenSettings()
            HomeEvents.OnDismissSettings -> onDismissSettings()
        }
    }

    private fun onClickEmergency() {
        when {
            permissionManager.isPermissionGranted() -> updateState { copy(shouldShowPermissionRequestModal = false) }
            else -> {
                permissionManager.requestPermission { permissionResult ->
                    updateState { copy(shouldShowPermissionRequestModal = permissionResult !is PermissionResult.Granted) }
                    if (permissionResult is PermissionResult.Granted) {
                        emitEffect(HomeEffects.NavigateToEmergency)
                    }
                }
            }
        }
    }

    private fun onOpenSettings() {
        updateState { copy(shouldShowPermissionRequestModal = false) }
        emitEffect(HomeEffects.NavigateToSettings)
    }

    private fun onDismissSettings() {
        updateState { copy(shouldShowPermissionRequestModal = false) }
    }
}