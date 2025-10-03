package us.docbee.docbeeapp.presentation.home

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import us.docbee.docbeeapp.domain.models.directory.ContactResult
import us.docbee.docbeeapp.domain.usecases.GetUserContactsUseCase
import us.docbee.docbeeapp.presentation.core.BaseViewModel
import us.docbee.docbeeapp.presentation.home.effects.HomeEffects
import us.docbee.docbeeapp.presentation.home.events.HomeEvents
import us.docbee.docbeeapp.presentation.home.states.UiState
import us.docbee.docbeeapp.utils.ui.permissions.LocationPermissionManager
import us.docbee.docbeeapp.utils.ui.permissions.PermissionResult

class HomeViewModel(
    private val permissionManager: LocationPermissionManager,
    private val fetchContactsUseCase: GetUserContactsUseCase
) : BaseViewModel<UiState, HomeEvents, HomeEffects>(UiState()) {

    override fun onEvent(event: HomeEvents) {
        when (event) {
            HomeEvents.OnClickEmergency -> onClickEmergency()
            HomeEvents.OnOpenSettings -> onOpenSettings()
            HomeEvents.OnDismissSettings -> onDismissSettings()
            HomeEvents.OnClickContacts -> {
                updateState { copy(showContactMissing = false) }
                emitEffect(HomeEffects.NavigateToDirectory)
            }
            HomeEvents.OnCancelContacts -> updateState { copy(showContactMissing = false) }
        }
    }

    private fun onClickEmergency() {
        when {
            permissionManager.isPermissionGranted() -> {
                updateState { copy(shouldShowPermissionRequestModal = false) }
                validateUserContacts { emitEffect(HomeEffects.NavigateToEmergency) }
            }
            else -> {
                permissionManager.requestPermission { permissionResult ->
                    updateState { copy(shouldShowPermissionRequestModal = permissionResult !is PermissionResult.Granted) }
                    if (permissionResult is PermissionResult.Granted) {
                        validateUserContacts { emitEffect(HomeEffects.NavigateToEmergency) }
                    }
                }
            }
        }
    }

    private fun validateUserContacts(callback: () -> Unit) {
        viewModelScope.launch {
            val contacts = fetchContactsUseCase.fetchUserContacts()
            when (contacts) {
                is ContactResult.Success -> {
                    updateState { copy(showContactMissing = false) }
                    callback()
                }
                is ContactResult.Empty -> updateState { copy(showContactMissing = true) }
                else -> Unit // TODO: Add Error State
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