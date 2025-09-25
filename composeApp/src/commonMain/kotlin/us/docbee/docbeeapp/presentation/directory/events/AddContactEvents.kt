package us.docbee.docbeeapp.presentation.directory.events

import us.docbee.docbeeapp.domain.models.Country

sealed class AddContactEvents {
    data object OnInit: AddContactEvents()
    data object OnBackPressed: AddContactEvents()
    data class OnChangeNameField(val name: String): AddContactEvents()
    data class OnChangeLastNameField(val lastName: String): AddContactEvents()
    data class OnChangeEmailField(val email: String): AddContactEvents()
    data class OnChangeDateOfBirthField(val dateOfBirth: Long): AddContactEvents()
    data class OnCountryCodeField(val country: Country): AddContactEvents()
    data object CountryCodeEvent: AddContactEvents()
    data object CloseCountryCodeEvent: AddContactEvents()
    data class OnChangePhoneNumberField(val phoneNumber: String): AddContactEvents()
    data class OnChangeAddressField(val address: String): AddContactEvents()
    data class OnChangeSearchField(val search: String): AddContactEvents()
    data object OnSaveClick: AddContactEvents()
}