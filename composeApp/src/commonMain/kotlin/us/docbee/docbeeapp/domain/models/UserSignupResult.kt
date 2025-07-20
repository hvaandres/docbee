package us.docbee.docbeeapp.domain.models

sealed class UserSignupResult {
    data class Success(val uid: String): UserSignupResult()
    data class SignupFormValidation(
        val name: SimpleTextValidation,
        val lastName: SimpleTextValidation,
        val email: EmailTextValidation,
        val dateOfBirth: DateOfBirthValidation,
        val phoneNumber: SimpleTextValidation,
        val password: PasswordValidation
    ): UserSignupResult()
    data object AlreadyUsed: UserSignupResult()
    data object WeakPassword: UserSignupResult()
    data object Error: UserSignupResult()
}

sealed class SimpleTextValidation {
    data object Valid: SimpleTextValidation()
    data object InvalidLength: SimpleTextValidation()
    data object EmptyValue: SimpleTextValidation()
}

sealed class EmailTextValidation {
    data object Valid: EmailTextValidation()
    data object EmptyValue: EmailTextValidation()
    data object InvalidEmail: EmailTextValidation()
}

sealed class DateOfBirthValidation {
    data object UnderAge: DateOfBirthValidation()
    data object EmptyValue: DateOfBirthValidation()
    data object InvalidDate: DateOfBirthValidation()
    data object Valid: DateOfBirthValidation()
}

sealed class PasswordValidation {
    data object EmptyValue: PasswordValidation()
    data object InvalidLength: PasswordValidation()
    data object NoLowercaseAndUppercase: PasswordValidation()
    data object NoNumber: PasswordValidation()
    data object NoSpecialCharacter: PasswordValidation()
    data object Valid: PasswordValidation()
}

