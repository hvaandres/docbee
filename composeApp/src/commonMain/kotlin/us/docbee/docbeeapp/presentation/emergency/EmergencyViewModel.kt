package us.docbee.docbeeapp.presentation.emergency

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import us.docbee.docbeeapp.domain.models.EmergencyNotificationResult
import us.docbee.docbeeapp.domain.usecases.NotifyEmergencyUseCase
import us.docbee.docbeeapp.presentation.core.BaseViewModel
import us.docbee.docbeeapp.presentation.emergency.effects.EmergencyEffects
import us.docbee.docbeeapp.presentation.emergency.events.EmergencyEvents
import us.docbee.docbeeapp.presentation.emergency.states.UiState

class EmergencyViewModel(
    private val notifyEmergencyUseCase: NotifyEmergencyUseCase
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
            when (val result = notifyEmergencyUseCase.sendNotification()) {
                is EmergencyNotificationResult.Sent -> updateState { copy(contacts = result.contacts) }
                is EmergencyNotificationResult.Error -> emitEffect(EmergencyEffects.NavigateBack)
            }
        }
    }
}