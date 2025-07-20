package us.docbee.docbeeapp.domain.usecases

import us.docbee.docbeeapp.domain.models.DateOfBirthValidation
import us.docbee.docbeeapp.domain.models.EmailTextValidation
import us.docbee.docbeeapp.domain.models.PasswordValidation
import us.docbee.docbeeapp.domain.models.SimpleTextValidation
import us.docbee.docbeeapp.domain.models.UserSignupResult
import us.docbee.docbeeapp.domain.models.signup.SignUpParams
import us.docbee.docbeeapp.domain.repositories.EmailAuthRepository
import us.docbee.docbeeapp.utils.PASSWORD_MINIMUM_LENGTH
import us.docbee.docbeeapp.utils.PHONE_MAXIMUM_LENGTH
import us.docbee.docbeeapp.utils.PHONE_MINIMUM_LENGTH
import us.docbee.docbeeapp.utils.hasSpecialChars
import us.docbee.docbeeapp.utils.isValidEmail

class EmailSignupUseCase(
    private val emailAuthRepository: EmailAuthRepository
) {
    suspend fun createUser(userInformation: SignUpParams): UserSignupResult {
        val nameValidation = validateNames(userInformation.name)
        val lastNameValidation = validateNames(userInformation.lastName)
        val emailValidation = validateEmail(userInformation.email)
        val dateOfBirthValidation = validateDateOfBirth(userInformation.dateOfBirth)
        val phoneNumberValidation = validatePhoneNumber(userInformation.phoneNumber)
        val passwordValidation = validatePassword(userInformation.password)

        if (nameValidation !is SimpleTextValidation.Valid || lastNameValidation !is SimpleTextValidation.Valid
            || emailValidation !is EmailTextValidation.Valid || dateOfBirthValidation !is DateOfBirthValidation.Valid
            || phoneNumberValidation !is SimpleTextValidation.Valid || passwordValidation !is PasswordValidation.Valid
        ) {
            return UserSignupResult.SignupFormValidation(
                name = nameValidation,
                lastName = lastNameValidation,
                email = emailValidation,
                dateOfBirth = dateOfBirthValidation,
                phoneNumber = phoneNumberValidation,
                password = passwordValidation
            )
        }

        return emailAuthRepository.signup(userInformation.email, userInformation.password)
    }

    private fun validateNames(name: String): SimpleTextValidation {
        return if (name.isEmpty()) {
            SimpleTextValidation.InvalidLength
        } else {
            SimpleTextValidation.Valid
        }
    }

    private fun validateEmail(email: String): EmailTextValidation {
        return when {
            email.isEmpty() -> EmailTextValidation.EmptyValue
            !isValidEmail(email) -> EmailTextValidation.InvalidEmail
            else -> EmailTextValidation.Valid
        }
    }

    private fun validateDateOfBirth(date: String): DateOfBirthValidation {
        // TODO WIP validate age and date
        return when {
            date.isEmpty() -> DateOfBirthValidation.EmptyValue
            else -> DateOfBirthValidation.Valid
        }
    }

    private fun validatePhoneNumber(phone: String): SimpleTextValidation {
        return when {
            phone.isEmpty() -> SimpleTextValidation.EmptyValue
            phone.length < PHONE_MINIMUM_LENGTH || phone.length > PHONE_MAXIMUM_LENGTH -> SimpleTextValidation.InvalidLength
            else -> SimpleTextValidation.Valid
        }
    }

    private fun validatePassword(password: String): PasswordValidation {
        return when {
            password.isEmpty() -> PasswordValidation.EmptyValue
            password.length < PASSWORD_MINIMUM_LENGTH -> PasswordValidation.InvalidLength
            !password.any { it.isLowerCase() } || !password.any { it.isUpperCase() } -> PasswordValidation.NoLowercaseAndUppercase
            !password.any { it.isDigit() } -> PasswordValidation.NoNumber
            !hasSpecialChars(password) -> PasswordValidation.NoSpecialCharacter
            else -> PasswordValidation.Valid
        }
    }
}