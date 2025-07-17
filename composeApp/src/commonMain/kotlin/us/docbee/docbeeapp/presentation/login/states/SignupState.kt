package us.docbee.docbeeapp.presentation.login.states

import us.docbee.docbeeapp.presentation.components.inputs.countrycodefield.PhoneInputState

data class SignupState(
    var name: String = "",
    var nameError: String = "",
    var lastName: String = "",
    var lastNameError: String = "",
    var email: String = "",
    var emailError: String = "",
    var dateOfBirth: String = "",
    var dateOfBirthError: String = "",
    var password: String = "",
    var passwordError: String = "",
    var phoneState: PhoneInputState = PhoneInputState()
)