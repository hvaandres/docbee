package us.docbee.docbeeapp.presentation.login.states

data class SignupState(
    var name: String = "",
    var lastName: String = "",
    var email: String = "",
    var dateOfBirth: String = "",
    var phoneNumber: String = "",
    var password: String = ""
)