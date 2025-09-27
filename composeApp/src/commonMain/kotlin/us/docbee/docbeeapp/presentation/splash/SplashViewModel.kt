package us.docbee.docbeeapp.presentation.splash

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import us.docbee.docbeeapp.domain.models.splash.SessionResult
import us.docbee.docbeeapp.domain.usecases.GetUserSessionStatus
import us.docbee.docbeeapp.presentation.core.BaseViewModel
import us.docbee.docbeeapp.presentation.splash.effects.SplashEffects
import us.docbee.docbeeapp.presentation.splash.events.SplashEvents

class SplashViewModel(
    private val sessionStatus: GetUserSessionStatus
) : BaseViewModel<Unit, SplashEvents, SplashEffects>(initialState = Unit) {

    init {
        onEvent(SplashEvents.OnInit)
    }

    override fun onEvent(event: SplashEvents) {
        when (event) {
            SplashEvents.OnInit -> validateIsUserLogged()
        }
    }

    private fun validateIsUserLogged() {
        viewModelScope.launch {
            when(sessionStatus.checkSessionStatus()) {
                is SessionResult.UserLogged -> emitEffect(SplashEffects.NavigateToDashboard)
                is SessionResult.UserNoLogged -> emitEffect(SplashEffects.NavigateToAuthenticate)
            }
        }
    }
}