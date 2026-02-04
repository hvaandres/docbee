package us.docbee.docbeeapp.presentation.login

import androidx.lifecycle.viewModelScope
import docbee.composeapp.generated.resources.Res
import docbee.composeapp.generated.resources.login_form_invalid_credentials_error
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import us.docbee.docbeeapp.domain.models.login.LoginParams
import us.docbee.docbeeapp.domain.models.login.LoginResult
import us.docbee.docbeeapp.domain.models.login.LoginType
import us.docbee.docbeeapp.domain.usecases.login.LoginUseCase
import us.docbee.docbeeapp.presentation.core.BaseViewModel
import us.docbee.docbeeapp.presentation.login.effects.AuthEffects
import us.docbee.docbeeapp.presentation.login.events.AuthEvents

class AuthViewModel(
    private val loginAuthUseCase: LoginUseCase
): BaseViewModel<Unit, AuthEvents, AuthEffects>(Unit) {
    override fun onEvent(event: AuthEvents) {
        when(event) {
            is AuthEvents.OnGoogleLogin -> performLogin(LoginType.GOOGLE)
            is AuthEvents.OnAppleLogin -> performLogin(LoginType.APPLE)
            is AuthEvents.OnSuccessNavigation -> emitEffect(AuthEffects.NavigateToDashboard)
        }
    }

    private fun performLogin(type: LoginType) {
        viewModelScope.launch {
            val response = loginAuthUseCase.login(LoginParams(type = type))
            when (response) {
                is LoginResult.Success -> emitEffect(AuthEffects.NavigateToDashboard)
                else -> emitEffect(AuthEffects.ShowErrorMessage(getString(Res.string.login_form_invalid_credentials_error)))
            }
        }
    }
}