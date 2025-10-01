package us.docbee.docbeeapp.presentation.emergency

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import us.docbee.docbeeapp.domain.models.location.LocationResult
import us.docbee.docbeeapp.domain.usecases.FetchLocationUseCase
import us.docbee.docbeeapp.presentation.core.BaseViewModel
import us.docbee.docbeeapp.presentation.emergency.effects.EmergencyEffects
import us.docbee.docbeeapp.presentation.emergency.events.EmergencyEvents
import us.docbee.docbeeapp.presentation.emergency.states.UiState

class EmergencyViewModel(
    private val locationUseCase: FetchLocationUseCase
): BaseViewModel<UiState, EmergencyEvents, EmergencyEffects>(UiState()) {

    init {
        onEvent(EmergencyEvents.OnInit)
    }

    override fun onEvent(event: EmergencyEvents) {
        when (event) {
            is EmergencyEvents.OnInit -> onInitEmergency()
            is EmergencyEvents.OnBackPressed -> emitEffect(EmergencyEffects.NavigateBack)
            is EmergencyEvents.OnClickImSafe -> emitEffect(EmergencyEffects.NotifyImSafe)
        }
    }

    private fun onInitEmergency() {
        viewModelScope.launch {
            when (val result = locationUseCase.fetchCurrentLocation()) {
                is LocationResult.Success -> println("Location ${result.location}")
                is LocationResult.Error, is LocationResult.MissingPermission -> emitEffect(EmergencyEffects.NavigateBack)
            }
        }
    }
}