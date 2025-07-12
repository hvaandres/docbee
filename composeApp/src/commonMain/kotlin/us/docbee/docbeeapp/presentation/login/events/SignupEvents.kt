package us.docbee.docbeeapp.presentation.login.events

sealed class SignupEvents {
    data class OnChangeNameField(val name: String): SignupEvents()
    data class OnChangeLastNameField(val lastName: String): SignupEvents()
    data class OnChangeEmailField(val email: String): SignupEvents()
    data class OnChangeDateOfBirthField(val dateOfBirth: Long): SignupEvents()
    data class OnChangePhoneNumberField(val phoneNumber: String): SignupEvents()
    data class OnChangePasswordField(val password: String): SignupEvents()
    data object OnResetEvent: SignupEvents()
    data object OnSignupClickButton: SignupEvents()
}