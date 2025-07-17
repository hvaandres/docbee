package us.docbee.docbeeapp.presentation.login

import androidx.lifecycle.viewModelScope
import docbee.composeapp.generated.resources.Res
import docbee.composeapp.generated.resources.general_unknown_error_label
import docbee.composeapp.generated.resources.signup_form_register_date_birth_error
import docbee.composeapp.generated.resources.signup_form_register_email_already_used_error
import docbee.composeapp.generated.resources.signup_form_register_email_error
import docbee.composeapp.generated.resources.signup_form_register_lastname_error
import docbee.composeapp.generated.resources.signup_form_register_name_error
import docbee.composeapp.generated.resources.signup_form_register_password_empty_error
import docbee.composeapp.generated.resources.signup_form_register_password_length_error
import docbee.composeapp.generated.resources.signup_form_register_password_no_number_error
import docbee.composeapp.generated.resources.signup_form_register_password_no_special_chars_error
import docbee.composeapp.generated.resources.signup_form_register_password_no_uppercase_lowercase_error
import docbee.composeapp.generated.resources.signup_form_register_phone_number_empty_error
import docbee.composeapp.generated.resources.signup_form_register_phone_number_invalid_number_error
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import us.docbee.docbeeapp.domain.models.Country
import us.docbee.docbeeapp.domain.models.DateOfBirthValidation
import us.docbee.docbeeapp.domain.models.EmailTextValidation
import us.docbee.docbeeapp.domain.models.PasswordValidation
import us.docbee.docbeeapp.domain.models.SimpleTextValidation
import us.docbee.docbeeapp.domain.models.UserSignupResult
import us.docbee.docbeeapp.domain.models.signup.SignUpParams
import us.docbee.docbeeapp.domain.usecases.EmailSignupUseCase
import us.docbee.docbeeapp.domain.usecases.GetCountriesUseCase
import us.docbee.docbeeapp.presentation.components.inputs.countrycodefield.PhoneInputState
import us.docbee.docbeeapp.presentation.core.BaseViewModel
import us.docbee.docbeeapp.presentation.login.effects.SignupEffect
import us.docbee.docbeeapp.presentation.login.events.SignupEvents
import us.docbee.docbeeapp.presentation.login.states.SignupState
import us.docbee.docbeeapp.utils.COUNTRY_DEFAULT
import us.docbee.docbeeapp.utils.convertMillisWithoutTimeZone

class SignupViewModel(
    private val signupUseCase: EmailSignupUseCase,
    private val getCountriesUseCase: GetCountriesUseCase
) : BaseViewModel<SignupState, SignupEvents, SignupEffect>(SignupState()) {

    private var _fullCountries: MutableStateFlow<List<Country>> = MutableStateFlow(emptyList())

    init {
        onEvent(SignupEvents.OnInit)
    }

    override fun onEvent(event: SignupEvents) {
        when (event) {
            is SignupEvents.OnInit -> initData()
            is SignupEvents.OnChangeNameField -> onChangeName(event.name)
            is SignupEvents.OnChangeLastNameField -> onChangeLastName(event.lastName)
            is SignupEvents.OnChangeEmailField -> onChangeEmail(event.email)
            is SignupEvents.OnChangeDateOfBirthField -> onChangeDateOfBirth(event.dateOfBirth)
            is SignupEvents.OnChangePhoneNumberField -> onChangePhoneNumber(event.phoneNumber)
            is SignupEvents.OnCountryCodeField -> onChangeCountryCode(event.country)
            is SignupEvents.OnChangePasswordField -> onChangePassword(event.password)
            is SignupEvents.OnResetEvent -> onResetData()
            is SignupEvents.OnSignupClickButton -> performSignup()
            is SignupEvents.CountryCodeEvent -> emitEffect(SignupEffect.OpenCountrySelector)
            is SignupEvents.CloseCountryCodeEvent -> onResetCountryCodeSelector()
            is SignupEvents.OnChangeSearchField -> onSearchCountry(event.search)
        }
    }

    private fun initData() {
        viewModelScope.launch {
            val response = getCountriesUseCase.fetchCountries()
            updateState {
                copy(
                    phoneState = phoneState.copy(
                        countries = response,
                        selectedCountry = response.find { it.isoCode == COUNTRY_DEFAULT }
                    )
                )
            }
            _fullCountries.value = response
        }
    }

    private fun onChangeName(name: String) {
        updateState { copy(name = name, nameError = "") }
    }

    private fun onChangeLastName(lastName: String) {
        updateState { copy(lastName = lastName, lastNameError = "") }
    }

    private fun onChangeEmail(email: String) {
        updateState { copy(email = email, emailError = "") }
    }

    private fun onChangeDateOfBirth(dateOfBirth: Long) {
        val dateOfBirthFormatted = convertMillisWithoutTimeZone(dateOfBirth)
        updateState { copy(dateOfBirth = dateOfBirthFormatted, dateOfBirthError = "") }
    }

    private fun onChangePhoneNumber(phoneNumber: String) {
        updateState {
            copy(
                phoneState = phoneState.copy(phoneNumber = phoneNumber, error = "")
            )
        }
    }

    private fun onChangeCountryCode(country: Country) {
        updateState {
            copy(
                phoneState = phoneState.copy(selectedCountry = country)
            )
        }
        onSearchCountry("")
        emitEffect(SignupEffect.CloseCountrySelector)
    }

    private fun onSearchCountry(search: String) {
        val filteredList = if (search.isEmpty()) {
            _fullCountries.value
        } else {
            _fullCountries.value
                .filter {
                    it.callingCode.contains(search, ignoreCase = true)
                            || it.name.contains(search, ignoreCase = true)
                }
        }
        updateState {
            copy(
                phoneState = phoneState.copy(
                    countries = filteredList,
                    searchCountryValue = search
                )
            )
        }
    }

    private fun onResetCountryCodeSelector() {
        onSearchCountry("")
        emitEffect(SignupEffect.CloseCountrySelector)
    }

    private fun onChangePassword(password: String) {
        updateState { copy(password = password, passwordError = "") }
    }

    private fun onResetData() {
        updateState {
            SignupState(
                phoneState = PhoneInputState(
                    countries = _fullCountries.value,
                    selectedCountry = _fullCountries.value.find { it.isoCode == COUNTRY_DEFAULT }
                )
            )
        }
    }

    private fun performSignup() {
        viewModelScope.launch {
            val state = uiState.value
            val signupParams = SignUpParams(
                email = state.email,
                name = state.name,
                lastName = state.lastName,
                dateOfBirth = state.dateOfBirth,
                phoneNumber = "${state.phoneState.selectedCountry?.callingCode}${state.phoneState.phoneNumber}",
                password = state.password
            )
            when (val response = signupUseCase.createUser(signupParams)) {
                is UserSignupResult.Success -> emitEffect(SignupEffect.NavigateToDashboard)
                is UserSignupResult.SignupFormValidation -> verifyValidations(response)
                is UserSignupResult.AlreadyUsed -> {
                    emitEffect(
                        SignupEffect.ShowErrorMessage(
                            getString(Res.string.signup_form_register_email_already_used_error)
                        )
                    )
                }

                is UserSignupResult.Error, is UserSignupResult.WeakPassword -> {
                    emitEffect(
                        SignupEffect.ShowErrorMessage(
                            getString(Res.string.general_unknown_error_label)
                        )
                    )
                }
            }
        }
    }

    private suspend fun verifyValidations(response: UserSignupResult.SignupFormValidation) {
        val nameError = checkNameValidation(response.name)
        val lastNameError = checkLastNameValidation(response.lastName)
        val emailError = checkEmailValidation(response.email)
        val dateOfBirthError = checkDateOfBirthValidation(response.dateOfBirth)
        val phoneNumberError = checkPhoneNumberValidation(response.phoneNumber)
        val passwordError = checkPasswordValidation(response.password)
        updateState {
            copy(
                nameError = nameError,
                lastNameError = lastNameError,
                emailError = emailError,
                dateOfBirthError = dateOfBirthError,
                phoneState = phoneState.copy(error = phoneNumberError),
                passwordError = passwordError
            )
        }
    }

    private suspend fun checkNameValidation(name: SimpleTextValidation): String {
        return if (name !is SimpleTextValidation.Valid) {
            getString(Res.string.signup_form_register_name_error)
        } else {
            ""
        }
    }

    private suspend fun checkLastNameValidation(lastName: SimpleTextValidation): String {
        return if (lastName !is SimpleTextValidation.Valid) {
            getString(Res.string.signup_form_register_lastname_error)
        } else {
            ""
        }
    }

    private suspend fun checkEmailValidation(email: EmailTextValidation): String {
        return if (email !is EmailTextValidation.Valid) {
            getString(Res.string.signup_form_register_email_error)
        } else {
            ""
        }
    }

    private suspend fun checkDateOfBirthValidation(dateOfBirth: DateOfBirthValidation): String {
        return if (dateOfBirth !is DateOfBirthValidation.Valid) {
            getString(Res.string.signup_form_register_date_birth_error)
        } else {
            ""
        }
    }

    private suspend fun checkPhoneNumberValidation(phoneNumber: SimpleTextValidation): String {
        return when (phoneNumber) {
            is SimpleTextValidation.EmptyValue -> getString(Res.string.signup_form_register_phone_number_empty_error)
            is SimpleTextValidation.InvalidLength -> getString(Res.string.signup_form_register_phone_number_invalid_number_error)
            is SimpleTextValidation.Valid -> ""
        }
    }

    private suspend fun checkPasswordValidation(password: PasswordValidation): String {
        return when (password) {
            is PasswordValidation.EmptyValue -> getString(Res.string.signup_form_register_password_empty_error)
            is PasswordValidation.InvalidLength -> getString(Res.string.signup_form_register_password_length_error)
            is PasswordValidation.NoNumber -> getString(Res.string.signup_form_register_password_no_number_error)
            is PasswordValidation.NoLowercaseAndUppercase -> getString(Res.string.signup_form_register_password_no_uppercase_lowercase_error)
            is PasswordValidation.NoSpecialCharacter -> getString(Res.string.signup_form_register_password_no_special_chars_error)
            is PasswordValidation.Valid -> ""
        }
    }

}