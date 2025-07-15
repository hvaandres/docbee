package us.docbee.docbeeapp.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import us.docbee.docbeeapp.domain.usecases.EmailSignupUseCase
import us.docbee.docbeeapp.domain.usecases.GetCountriesUseCase
import us.docbee.docbeeapp.presentation.login.effects.SignupEffect
import us.docbee.docbeeapp.presentation.login.events.SignupEvents
import us.docbee.docbeeapp.presentation.login.states.SignupState
import us.docbee.docbeeapp.utils.COUNTRY_DEFAULT
import us.docbee.docbeeapp.utils.convertMillisWithoutTimeZone

class SignupViewModel(
    private val signupUseCase: EmailSignupUseCase,
    private val getCountriesUseCase: GetCountriesUseCase
) : ViewModel() {

    private var _state: MutableStateFlow<SignupState> = MutableStateFlow(SignupState())
    val state: StateFlow<SignupState> = _state

    private var _effect: MutableSharedFlow<SignupEffect> = MutableSharedFlow()
    val effect: SharedFlow<SignupEffect> = _effect

    init {
        onEvent(SignupEvents.OnInit)
    }

    fun onEvent(event: SignupEvents) {
        when (event) {
            is SignupEvents.OnInit -> initData()
            is SignupEvents.OnChangeNameField -> onChangeName(event.name)
            is SignupEvents.OnChangeLastNameField -> onChangeLastName(event.lastName)
            is SignupEvents.OnChangeEmailField -> onChangeEmail(event.email)
            is SignupEvents.OnChangeDateOfBirthField -> onChangeDateOfBirth(event.dateOfBirth)
            is SignupEvents.OnChangePhoneNumberField -> onChangePhoneNumber(event.phoneNumber)
            is SignupEvents.OnChangePasswordField -> onChangePassword(event.password)
            is SignupEvents.OnResetEvent -> onResetData()
            is SignupEvents.OnSignupClickButton -> performSignup()
        }
    }

    private fun initData() {
        viewModelScope.launch {
            val response = getCountriesUseCase.fetchCountries()
            _state.value = _state.value.copy(
                phoneState = _state.value.phoneState.copy(
                    countries = response,
                    selectedCountry = response.find { it.isoCode == COUNTRY_DEFAULT }
                )
            )
        }
    }

    private fun onChangeName(name: String) {
        _state.value = _state.value.copy(name = name)
    }

    private fun onChangeLastName(lastName: String) {
        _state.value = _state.value.copy(lastName = lastName)
    }

    private fun onChangeEmail(email: String) {
        _state.value = _state.value.copy(email = email)
    }

    private fun onChangeDateOfBirth(dateOfBirth: Long) {
        val dateOfBirthFormatted = convertMillisWithoutTimeZone(dateOfBirth)
        _state.value = _state.value.copy(dateOfBirth = dateOfBirthFormatted)
    }

    private fun onChangePhoneNumber(phoneNumber: String) {
        _state.value = _state.value.copy(
            phoneState = _state.value.phoneState.copy(
                phoneNumber = phoneNumber
            )
        )
    }

    private fun onChangePassword(password: String) {
        _state.value = _state.value.copy(password = password)
    }

    private fun onResetData() {
        _state.value = _state.value.copy(
            name = "",
            lastName = "",
            email = "",
            dateOfBirth = "",
            password = ""
        )
    }

    private fun performSignup() {

    }

    private fun sendEffect(effect: SignupEffect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }

}