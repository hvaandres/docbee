package us.docbee.docbeeapp.presentation.directory

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import us.docbee.docbeeapp.domain.models.Country
import us.docbee.docbeeapp.domain.models.directory.AddContactResult
import us.docbee.docbeeapp.domain.models.signup.ContactParams
import us.docbee.docbeeapp.domain.usecases.GetCountriesUseCase
import us.docbee.docbeeapp.domain.usecases.SaveUserContactUseCase
import us.docbee.docbeeapp.presentation.core.BaseViewModel
import us.docbee.docbeeapp.presentation.directory.effects.AddContactEffects
import us.docbee.docbeeapp.presentation.directory.events.AddContactEvents
import us.docbee.docbeeapp.presentation.directory.states.AddContactState
import us.docbee.docbeeapp.utils.COUNTRY_DEFAULT
import us.docbee.docbeeapp.utils.convertMillisWithoutTimeZone

class AddContactViewModel(
    private val getCountriesUseCase: GetCountriesUseCase,
    private val saveContactUseCase: SaveUserContactUseCase
) : BaseViewModel<AddContactState, AddContactEvents, AddContactEffects>(AddContactState()) {

    private var _fullCountries: MutableStateFlow<List<Country>> = MutableStateFlow(emptyList())

    init {
        onEvent(AddContactEvents.OnInit)
    }

    override fun onEvent(event: AddContactEvents) {
        when (event) {
            is AddContactEvents.OnInit -> initData()
            is AddContactEvents.OnBackPressed -> emitEffect(AddContactEffects.NavigateBack)
            is AddContactEvents.OnChangeNameField -> onChangeName(event.name)
            is AddContactEvents.OnChangeLastNameField -> onChangeLastName(event.lastName)
            is AddContactEvents.OnChangeEmailField -> onChangeEmail(event.email)
            is AddContactEvents.OnChangeDateOfBirthField -> onChangeDateOfBirth(event.dateOfBirth)
            is AddContactEvents.OnChangePhoneNumberField -> onChangePhoneNumber(event.phoneNumber)
            is AddContactEvents.OnCountryCodeField -> onChangeCountryCode(event.country)
            is AddContactEvents.OnChangeAddressField -> onChangeAddress(event.address)
            is AddContactEvents.OnSaveClick -> performSaveContact()
            is AddContactEvents.CountryCodeEvent -> emitEffect(AddContactEffects.OpenCountrySelector)
            is AddContactEvents.CloseCountryCodeEvent -> onResetCountryCodeSelector()
            is AddContactEvents.OnChangeSearchField -> onSearchCountry(event.search)
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
        emitEffect(AddContactEffects.CloseCountrySelector)
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

    private fun onChangeAddress(address: String) {
        updateState { copy(address = address, addressError = "") }
    }

    private fun onResetCountryCodeSelector() {
        onSearchCountry("")
        emitEffect(AddContactEffects.CloseCountrySelector)
    }

    private fun performSaveContact() {
        viewModelScope.launch {
            val state = uiState.value
            val contactParam = ContactParams(
                name = state.name,
                lastName = state.lastName,
                email = state.email,
                dateOfBirth = state.dateOfBirth,
                phoneNumber = "${state.phoneState.selectedCountry?.callingCode}${state.phoneState.phoneNumber}",
                address = state.address
            )
            when (val response = saveContactUseCase.saveUserContact(contactParam)) {
                is AddContactResult.Success -> emitEffect(AddContactEffects.NavigateToDirectory)
                else -> Unit
            }
        }
    }
}