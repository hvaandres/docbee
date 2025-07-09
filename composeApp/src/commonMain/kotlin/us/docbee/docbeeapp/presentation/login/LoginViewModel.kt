package us.docbee.docbeeapp.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import us.docbee.docbeeapp.domain.models.UserAuthResult
import us.docbee.docbeeapp.domain.models.UserSignupResult
import us.docbee.docbeeapp.domain.usecases.EmailAuthUseCase
import us.docbee.docbeeapp.domain.usecases.EmailSignupUseCase
import us.docbee.docbeeapp.presentation.login.effects.LoginEffect
import us.docbee.docbeeapp.presentation.login.events.LoginEvents
import us.docbee.docbeeapp.presentation.login.states.LoginState

class LoginViewModel(
    private val loginAuth: EmailAuthUseCase,
    private val signupUser: EmailSignupUseCase
) : ViewModel() {
    private var _state: MutableStateFlow<LoginState> = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state

    private var _effect: MutableSharedFlow<LoginEffect> = MutableSharedFlow()
    val effect: SharedFlow<LoginEffect> = _effect

    fun onEvent(event: LoginEvents) {
        when (event) {
            is LoginEvents.OnChangeEmailField -> onChangeEmail(event.email)
            is LoginEvents.OnLoginClickButton -> performLogin()
            is LoginEvents.OnDismissModalError -> sendEffect(LoginEffect.HideErrorMessage)
            is LoginEvents.OnSignupClickButton -> performSignup()
        }
    }

    private fun onChangeEmail(email: String) {
        _state.value = _state.value.copy(email = email)
        if (!email.contains("@")) {
            _state.value = _state.value.copy(isButtonEnabled = false, isEmailInvalid = true)
        } else {
            _state.value = _state.value.copy(isEmailInvalid = false)
        }
    }

    private fun performLogin() {
        viewModelScope.launch {
            val response = loginAuth.authenticate(email = _state.value.email, password = "abcd1234")
            when (response) {
                is UserAuthResult.Success -> sendEffect(LoginEffect.NavigateToDashboard)
                is UserAuthResult.InvalidCredentials -> sendEffect(LoginEffect.ShowErrorMessage("Invalid Credentials"))
                is UserAuthResult.Error -> sendEffect(LoginEffect.ShowErrorMessage("Error"))
            }
        }
    }

    private fun performSignup() {
        viewModelScope.launch {
            val response = signupUser.createUser(email = _state.value.email, password = "Qwerty123.")
            when (response) {
                is UserSignupResult.Success -> sendEffect(LoginEffect.NavigateToDashboard)
                is UserSignupResult.AlreadyUsed -> sendEffect(LoginEffect.ShowErrorMessage("Already in use"))
                is UserSignupResult.WeakPassword -> sendEffect(LoginEffect.ShowErrorMessage("Weak password"))
                is UserSignupResult.Error -> sendEffect(LoginEffect.ShowErrorMessage("Error"))
            }
        }
    }

    private fun sendEffect(effect: LoginEffect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }
}