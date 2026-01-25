package us.docbee.docbeeapp.presentation.home

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import us.docbee.docbeeapp.domain.models.directory.ContactResult
import us.docbee.docbeeapp.domain.usecases.GetUserContactsUseCase
import us.docbee.docbeeapp.presentation.core.BaseViewModel
import us.docbee.docbeeapp.presentation.home.effects.HomeEffects
import us.docbee.docbeeapp.presentation.home.events.HomeEvents
import us.docbee.docbeeapp.presentation.home.states.UiState
import us.docbee.docbeeapp.utils.ui.managers.ClickCounterManager
import us.docbee.docbeeapp.utils.ui.permissions.LocationPermissionManager
import us.docbee.docbeeapp.utils.ui.permissions.PermissionResult

class HomeViewModel(
    private val permissionManager: LocationPermissionManager,
    private val fetchContactsUseCase: GetUserContactsUseCase
) : BaseViewModel<UiState, HomeEvents, HomeEffects>(UiState()) {

    private val requiredClicks = 3
    private val clickEmergencyCounterManager = ClickCounterManager(requiredClicks = requiredClicks)

    override fun onEvent(event: HomeEvents) {
        when (event) {
            HomeEvents.OnValidateRequirements -> onInitHome()
            HomeEvents.OnClickEmergency -> onClickEmergency()
            HomeEvents.OnOpenSettings -> onOpenSettings()
            HomeEvents.OnClickContacts -> onClickContacts()
        }
    }

    private fun onInitHome() {
        when {
            permissionManager.isPermissionGranted() -> {
                updateState { copy(isPermissionNotGranted = false) }
                validateUserContacts()
            }
            else -> {
                permissionManager.requestPermission { permissionResult ->
                    updateState {
                        copy(isPermissionNotGranted = permissionResult !is PermissionResult.Granted)
                    }
                    if (permissionResult is PermissionResult.Granted) {
                        validateUserContacts()
                    }
                }
            }
        }
    }

    private fun onClickEmergency() {
        clickEmergencyCounterManager.onClick(
            scope = viewModelScope,
            onCountChanged = { times ->
                val remainingClicks = requiredClicks - times
                updateState { copy(emergencyRemainingClicks = remainingClicks) }
            },
            onThresholdReached = {
                emitEffect(HomeEffects.NavigateToEmergency)
            }
        )
    }

    private fun validateUserContacts() {
        viewModelScope.launch {
            val contacts = fetchContactsUseCase.fetchUserContacts()
            when (contacts) {
                is ContactResult.Success -> updateState { copy(showContactMissing = false) }
                is ContactResult.Empty -> updateState { copy(showContactMissing = true) }
                else -> Unit // TODO: Add Error State
            }
        }
    }

    private fun onOpenSettings() {
        emitEffect(HomeEffects.NavigateToSettings)
    }

    private fun onClickContacts() {
        emitEffect(HomeEffects.NavigateToDirectory)
    }
}