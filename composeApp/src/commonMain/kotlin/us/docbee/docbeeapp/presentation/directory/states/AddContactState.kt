package us.docbee.docbeeapp.presentation.directory.states

import us.docbee.docbeeapp.presentation.components.inputs.countrycodefield.PhoneInputState

data class AddContactState(
    var name: String = "",
    var nameError: String = "",
    var lastName: String = "",
    var lastNameError: String = "",
    var email: String = "",
    var emailError: String = "",
    var dateOfBirth: String = "",
    var dateOfBirthError: String = "",
    var address: String = "",
    var addressError: String = "",
    var phoneState: PhoneInputState = PhoneInputState()
)