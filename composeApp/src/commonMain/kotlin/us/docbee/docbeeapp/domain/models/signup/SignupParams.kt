package us.docbee.docbeeapp.domain.models.signup

data class SignUpParams(
    val name: String,
    val email: String,
    val lastName: String,
    val dateOfBirth: String,
    val phoneNumber: String,
    val password: String
)