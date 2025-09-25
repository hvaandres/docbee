package us.docbee.docbeeapp.domain.models.signup

data class ContactParams(
    val name: String,
    val lastName: String,
    val email: String,
    val dateOfBirth: String,
    val phoneNumber: String,
    val address: String
)