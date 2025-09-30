package us.docbee.docbeeapp.presentation.settings

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import us.docbee.docbeeapp.domain.models.session.LogoutSessionResult
import us.docbee.docbeeapp.domain.usecases.LogoutUseCase
import us.docbee.docbeeapp.presentation.core.BaseViewModel
import us.docbee.docbeeapp.presentation.settings.effects.SettingsEffect
import us.docbee.docbeeapp.presentation.settings.events.SettingsEvent

class SettingsViewModel(
    private val logoutUseCase: LogoutUseCase
) : BaseViewModel<Unit, SettingsEvent, SettingsEffect>(Unit) {

    override fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.OnClickLogout -> doLogout()
            else -> Unit
        }
    }

    private fun doLogout() {
        viewModelScope.launch {
            when (logoutUseCase.doLogout()) {
                is LogoutSessionResult.Success -> emitEffect(SettingsEffect.NavigateToLogin)
                is LogoutSessionResult.Error -> Unit // TODO: Add snack to show that something went wrong
            }
        }
    }
}