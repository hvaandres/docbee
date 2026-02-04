package us.docbee.docbeeapp.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import docbee.composeapp.generated.resources.Res
import docbee.composeapp.generated.resources.login_form_invalid_credentials
import docbee.composeapp.generated.resources.login_form_invalid_credentials_error
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import us.docbee.docbeeapp.domain.models.login.LoginParams
import us.docbee.docbeeapp.domain.models.login.LoginResult
import us.docbee.docbeeapp.domain.models.login.LoginType
import us.docbee.docbeeapp.domain.usecases.login.LoginUseCase
import us.docbee.docbeeapp.presentation.login.effects.LoginEffect
import us.docbee.docbeeapp.presentation.login.events.LoginEvents
import us.docbee.docbeeapp.presentation.login.states.LoginState

class LoginViewModel(
    private val loginAuthUseCase: LoginUseCase
) : ViewModel() {

    private var _state: MutableStateFlow<LoginState> = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state

    private var _effect: MutableSharedFlow<LoginEffect> = MutableSharedFlow()
    val effect: SharedFlow<LoginEffect> = _effect

    fun onEvent(event: LoginEvents) {
        when (event) {
            is LoginEvents.OnChangeEmailField -> onChangeEmail(event.email)
            is LoginEvents.OnChangePasswordField -> onChangePassword(event.password)
            is LoginEvents.OnRememberCheckBox -> onChangeRememberMeCheck(event.isChecked)
            is LoginEvents.OnEmailLogin -> performLogin()
            is LoginEvents.OnResetEvent -> onResetData()
            is LoginEvents.OnForgotPasswordClick -> Unit
        }
    }

    private fun onChangeEmail(email: String) {
        _state.value =
            _state.value.copy(email = email, isEmailInvalid = false, isPasswordInvalid = false)
    }

    private fun onChangePassword(password: String) {
        _state.value =
            _state.value.copy(password = password, isEmailInvalid = false, isPasswordInvalid = false)
    }

    private fun onChangeRememberMeCheck(isChecked: Boolean) {
        _state.value = _state.value.copy(rememberMe = isChecked)
    }

    private fun onResetData() {
        _state.value = LoginState()
    }

    private fun performLogin() {
        viewModelScope.launch {
            val response = loginAuthUseCase.login(
                LoginParams(
                    type = LoginType.EMAIL,
                    email = _state.value.email,
                    password = _state.value.password,
                    rememberMeCheck = _state.value.rememberMe
                )
            )
            when (response) {
                is LoginResult.InvalidEmail -> _state.value =
                    _state.value.copy(isEmailInvalid = true)

                is LoginResult.InvalidPassword -> _state.value =
                    _state.value.copy(isPasswordInvalid = true)

                is LoginResult.InvalidEmailAndPassword -> _state.value =
                    _state.value.copy(isEmailInvalid = true, isPasswordInvalid = true)

                is LoginResult.InvalidCredentials -> sendEffect(
                    LoginEffect.ShowErrorMessage(getString(Res.string.login_form_invalid_credentials))
                )

                is LoginResult.Error -> sendEffect(
                    LoginEffect.ShowErrorMessage(getString(Res.string.login_form_invalid_credentials_error))
                )

                is LoginResult.Success -> sendEffect(LoginEffect.NavigateToDashboard)
            }
        }
    }

    private fun sendEffect(effect: LoginEffect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }
}