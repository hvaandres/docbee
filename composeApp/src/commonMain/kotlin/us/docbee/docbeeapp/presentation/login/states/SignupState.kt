package us.docbee.docbeeapp.presentation.login.states

import us.docbee.docbeeapp.presentation.components.inputs.countrycodefield.PhoneInputState

data class SignupState(
    var name: String = "",
    var lastName: String = "",
    var email: String = "",
    var dateOfBirth: String = "",
    var password: String = "",
    var phoneState: PhoneInputState = PhoneInputState()
)